package com.jakduk.api.service;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.elasticsearch.*;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import com.jakduk.api.restcontroller.vo.search.*;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 */

@Service
public class SearchService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private JakdukProperties.Elasticsearch elasticsearchProperties;

	@Autowired
	private UrlGenerationUtils urlGenerationUtils;
	@Autowired
	private RestHighLevelClient highLevelClient;

	/**
	 * 통합 검색
	 *
	 * @param query    검색어
	 * @param from    페이지 시작 위치
	 * @param size    페이지 크기
	 * @return 검색 결과
	 */
	public SearchUnifiedResponse searchUnified(String query, String include, Integer from, Integer size, String preTags,
		String postTags) throws IOException {

		SearchUnifiedResponse searchUnifiedResponse = new SearchUnifiedResponse();
		Queue<Constants.SEARCH_INCLUDE_TYPE> searchOrder = new LinkedList<>();
		MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

		if (StringUtils.contains(include, Constants.SEARCH_INCLUDE_TYPE.ARTICLE.name())) {
			SearchRequest searchRequest = this.getArticleSearchRequestBuilder(query, from, size, preTags, postTags);
			multiSearchRequest.add(searchRequest);
			searchOrder.offer(Constants.SEARCH_INCLUDE_TYPE.ARTICLE);
		}

		if (StringUtils.contains(include, Constants.SEARCH_INCLUDE_TYPE.COMMENT.name())) {
			SearchRequest searchRequest = this.getCommentSearchRequestBuilder(query, from, size, preTags, postTags);
			multiSearchRequest.add(searchRequest);
			searchOrder.offer(Constants.SEARCH_INCLUDE_TYPE.COMMENT);
		}

		if (StringUtils.contains(include, Constants.SEARCH_INCLUDE_TYPE.GALLERY.name())) {
			SearchRequest searchRequest = this.getGallerySearchRequestBuilder(query, from, size < 10 ? 4 : size,
				preTags, postTags);
			multiSearchRequest.add(searchRequest);
			searchOrder.offer(Constants.SEARCH_INCLUDE_TYPE.GALLERY);
		}

		MultiSearchResponse multiSearchResponse = highLevelClient.msearch(multiSearchRequest, RequestOptions.DEFAULT);

		for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
			SearchResponse searchResponse = item.getResponse();
			Constants.SEARCH_INCLUDE_TYPE order = searchOrder.poll();

			if (item.isFailure())
				continue;

			if (!ObjectUtils.isEmpty(order)) {
				switch (order) {
					case ARTICLE:
						SearchArticleResult searchArticleResult = this.getArticleSearchResponse(searchResponse);
						searchUnifiedResponse.setArticleResult(searchArticleResult);
						break;
					case COMMENT:
						SearchCommentResult searchCommentResult = this.getCommentSearchResponse(searchResponse);
						searchUnifiedResponse.setCommentResult(searchCommentResult);
						break;
					case GALLERY:
						SearchGalleryResult searchGalleryResult = this.getGallerySearchResponse(searchResponse);
						searchUnifiedResponse.setGalleryResult(searchGalleryResult);
						break;
				}
			}
		}

		return searchUnifiedResponse;
	}

	public PopularSearchWordResult aggregateSearchWord(LocalDate gteDate, Integer size) throws IOException {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(0);
		searchSourceBuilder.query(
			QueryBuilders.rangeQuery("registerDate").gte(gteDate.toString())
		);
		searchSourceBuilder.aggregation(
			AggregationBuilders
				.terms("popular_word_aggs")
				.field("word")
				.size(size)
		);

		SearchRequest searchRequest = new SearchRequest(elasticsearchProperties.getIndexSearchWord());
		searchRequest.types(Constants.ES_TYPE_SEARCH_WORD);
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		Terms popularWordTerms = searchResponse.getAggregations().get("popular_word_aggs");

		List<EsTermsBucket> popularWords = popularWordTerms.getBuckets().stream()
			.map(entry -> {
				EsTermsBucket esTermsBucket = new EsTermsBucket();
				esTermsBucket.setKey(entry.getKeyAsString());
				esTermsBucket.setCount(entry.getDocCount());
				return esTermsBucket;
			})
			.collect(Collectors.toList());

		return new PopularSearchWordResult() {{
			setTook(searchResponse.getTook().getMillis());
			setPopularSearchWords(popularWords);
		}};
	}

	public void indexDocumentArticle(EsArticle esArticle) throws IOException {
		String id = esArticle.getId();

		IndexRequest indexRequest = new IndexRequest(elasticsearchProperties.getIndexBoard());
		indexRequest.id(id);
		indexRequest.source(ObjectMapperUtils.writeValueAsString(esArticle), XContentType.JSON);
		highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
	}

	public void deleteDocumentBoard(String id) throws IOException {
		DeleteRequest request = new DeleteRequest(elasticsearchProperties.getIndexBoard(), Constants.ES_TYPE_ARTICLE,
			id);
		DeleteResponse response = highLevelClient.delete(request, RequestOptions.DEFAULT);

		if (!response.status().equals(RestStatus.OK))
			log.warn("board id {} is not found. so can't delete it!", id);
	}

	public void indexDocumentBoardComment(EsComment esComment) throws IOException {
		String id = esComment.getId();
		String parentBoardId = esComment.getArticle().getId();

		IndexRequest indexRequest = new IndexRequest(elasticsearchProperties.getIndexBoard());
		indexRequest.id(id);
		indexRequest.routing(parentBoardId);
		indexRequest.source(ObjectMapperUtils.writeValueAsString(indexRequest), XContentType.JSON);
		highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
	}

	public void deleteDocumentBoardComment(String id) throws IOException {

		DeleteRequest deleteRequest = new DeleteRequest(elasticsearchProperties.getIndexBoard(), id);
		DeleteResponse response = highLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);

		if (!response.status().equals(RestStatus.OK))
			log.warn("comment id {} is not found. so can't delete it!", id);
	}

	// TODO : 구현 해야 함
	public void createDocumentJakduComment(EsJakduComment EsJakduComment) {
	}

	public void indexDocumentGallery(EsGallery esGallery) throws IOException {
		String id = esGallery.getId();

		IndexRequest indexRequest = new IndexRequest(elasticsearchProperties.getIndexGallery());
		indexRequest.id(id);
		indexRequest.source(ObjectMapperUtils.writeValueAsString(indexRequest), XContentType.JSON);
		highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
	}

	public void deleteDocumentGallery(String id) throws IOException {
		DeleteRequest request = new DeleteRequest(elasticsearchProperties.getIndexGallery(), Constants.ES_TYPE_GALLERY,
			id);
		DeleteResponse response = highLevelClient.delete(request, RequestOptions.DEFAULT);

		if (!response.status().equals(RestStatus.OK))
			log.warn("gallery id {} is not found. so can't delete it!", id);
	}

	public void indexDocumentSearchWord(EsSearchWord esSearchWord) throws IOException {
		IndexRequest request = new IndexRequest(elasticsearchProperties.getIndexSearchWord(),
			Constants.ES_TYPE_SEARCH_WORD);
		request.source(ObjectMapperUtils.writeValueAsString(esSearchWord), XContentType.JSON);
		highLevelClient.index(request, RequestOptions.DEFAULT);
	}

	private SearchRequest getArticleSearchRequestBuilder(String query, Integer from, Integer size, String preTags,
		String postTags) {

		HighlightBuilder highlightBuilder = new HighlightBuilder()
			.noMatchSize(Constants.SEARCH_NO_MATCH_SIZE)
			.fragmentSize(Constants.SEARCH_FRAGMENT_SIZE)
			.field("subject", Constants.SEARCH_FRAGMENT_SIZE, 0)
			.field("content", Constants.SEARCH_FRAGMENT_SIZE, 1);

		if (StringUtils.isNotBlank(preTags))
			highlightBuilder.preTags(preTags);

		if (StringUtils.isNotBlank(postTags))
			highlightBuilder.postTags(postTags);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.fetchSource(null, new String[] {"subject", "content"});
		searchSourceBuilder.query(
			QueryBuilders.boolQuery()
				.should(QueryBuilders.multiMatchQuery(query, "subject", "content").field("subject", 1.5f))
		);
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);
		searchSourceBuilder.highlighter(highlightBuilder);

		SearchRequest searchRequest = new SearchRequest(elasticsearchProperties.getIndexBoard());
		searchRequest.types(Constants.ES_TYPE_ARTICLE);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private SearchArticleResult getArticleSearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<ArticleSource> searchList = Arrays.stream(searchHits.getHits())
			.map(searchHit -> {
				Map<String, Object> sourceMap = searchHit.getSourceAsMap();
				EsArticleSource esArticleSource = ObjectMapperUtils.convertValue(sourceMap, EsArticleSource.class);
				esArticleSource.setScore(searchHit.getScore());

				Map<String, List<String>> highlight = this.getHighlight(searchHit.getHighlightFields().entrySet());
				esArticleSource.setHighlight(highlight);

				return esArticleSource;
			})
			.map(esArticleSource -> {
				ArticleSource articleSource = new ArticleSource();
				BeanUtils.copyProperties(esArticleSource, articleSource);

				if (!ObjectUtils.isEmpty(esArticleSource.getGalleries())) {
					List<BoardGallerySimple> boardGalleries = esArticleSource.getGalleries().stream()
						.sorted(Comparator.comparing(String::toString))
						.limit(1)
						.map(galleryId -> new BoardGallerySimple() {{
							setId(galleryId);
							setThumbnailUrl(
								urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, galleryId));
						}})
						.collect(Collectors.toList());

					articleSource.setGalleries(boardGalleries);
				}

				return articleSource;
			})
			.collect(Collectors.toList());

		return new SearchArticleResult() {{
			setTook(searchResponse.getTook().getMillis());
			setTotalCount(searchHits.getTotalHits().value);
			setArticles(searchList);
		}};
	}

	private SearchRequest getCommentSearchRequestBuilder(String query, Integer from, Integer size, String preTags,
		String postTags) {

		HighlightBuilder highlightBuilder = new HighlightBuilder()
			.noMatchSize(Constants.SEARCH_NO_MATCH_SIZE)
			.fragmentSize(Constants.SEARCH_FRAGMENT_SIZE)
			.field("content", Constants.SEARCH_FRAGMENT_SIZE, 1);

		if (StringUtils.isNotBlank(preTags))
			highlightBuilder.preTags(preTags);

		if (StringUtils.isNotBlank(postTags))
			highlightBuilder.postTags(postTags);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.fetchSource(null, new String[] {"content"});
		searchSourceBuilder.query(
			QueryBuilders.boolQuery()
				.must(QueryBuilders.matchQuery("content", query))
				.must(JoinQueryBuilders
					.hasParentQuery(Constants.ES_TYPE_ARTICLE, QueryBuilders.matchAllQuery(), false)
					.innerHit(new InnerHitBuilder())
				)
		);
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);
		searchSourceBuilder.highlighter(highlightBuilder);

		SearchRequest searchRequest = new SearchRequest(elasticsearchProperties.getIndexBoard());
		searchRequest.types(Constants.ES_TYPE_COMMENT);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private SearchCommentResult getCommentSearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<EsCommentSource> searchList = Arrays.stream(searchHits.getHits())
			.map(searchHit -> {
				Map<String, Object> sourceMap = searchHit.getSourceAsMap();
				EsCommentSource esCommentSource = ObjectMapperUtils.convertValue(sourceMap, EsCommentSource.class);
				esCommentSource.setScore(searchHit.getScore());

				if (!searchHit.getInnerHits().isEmpty()) {
					SearchHit[] innerSearchHits = searchHit.getInnerHits().get(Constants.ES_TYPE_ARTICLE).getHits();
					Map<String, Object> innerSourceMap = innerSearchHits[innerSearchHits.length - 1].getSourceAsMap();
					EsParentArticle esParentArticle = ObjectMapperUtils.convertValue(innerSourceMap,
						EsParentArticle.class);

					esCommentSource.setArticle(esParentArticle);
				}

				Map<String, List<String>> highlight = this.getHighlight(searchHit.getHighlightFields().entrySet());
				esCommentSource.setHighlight(highlight);

				return esCommentSource;
			})
			.collect(Collectors.toList());

		return new SearchCommentResult() {{
			setTook(searchResponse.getTook().getMillis());
			setTotalCount(searchHits.getTotalHits().value);
			setComments(searchList);
		}};
	}

	private SearchRequest getGallerySearchRequestBuilder(String query, Integer from, Integer size, String preTags,
		String postTags) {

		HighlightBuilder highlightBuilder = new HighlightBuilder()
			.noMatchSize(Constants.SEARCH_NO_MATCH_SIZE)
			.fragmentSize(Constants.SEARCH_FRAGMENT_SIZE)
			.field("name", Constants.SEARCH_FRAGMENT_SIZE, 0);

		if (StringUtils.isNotBlank(preTags))
			highlightBuilder.preTags(preTags);

		if (StringUtils.isNotBlank(postTags))
			highlightBuilder.postTags(postTags);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.fetchSource(null, new String[] {"name"});
		searchSourceBuilder.query(QueryBuilders.matchQuery("name", query));
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);
		searchSourceBuilder.highlighter(highlightBuilder);

		SearchRequest searchRequest = new SearchRequest(elasticsearchProperties.getIndexGallery());
		searchRequest.types(Constants.ES_TYPE_GALLERY);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private SearchGalleryResult getGallerySearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<EsGallerySource> searchList = Arrays.stream(searchHits.getHits())
			.map(searchHit -> {
				Map<String, Object> sourceMap = searchHit.getSourceAsMap();
				EsGallerySource esGallerySource = ObjectMapperUtils.convertValue(sourceMap, EsGallerySource.class);
				esGallerySource.setScore(searchHit.getScore());

				Map<String, List<String>> highlight = this.getHighlight(searchHit.getHighlightFields().entrySet());
				esGallerySource.setHighlight(highlight);

				return esGallerySource;
			})
			.collect(Collectors.toList());

		return new SearchGalleryResult() {{
			setTook(searchResponse.getTook().getMillis());
			setTotalCount(searchHits.getTotalHits().value);
			setGalleries(searchList);
		}};
	}

	private Map<String, List<String>> getHighlight(Set<Map.Entry<String, HighlightField>> entrySet) {
		Map<String, List<String>> highlight = new HashMap<>();

		for (Map.Entry<String, HighlightField> highlightField : entrySet) {
			List<String> fragments = new ArrayList<>();
			for (Text text : highlightField.getValue().fragments()) {
				fragments.add(text.string());
			}
			highlight.put(highlightField.getKey(), fragments);
		}

		return highlight;
	}

}

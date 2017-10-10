package com.jakduk.api.service;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.elasticsearch.*;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import com.jakduk.api.restcontroller.vo.search.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.highlight.HighlightField;
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

@Slf4j
@Service
public class SearchService {

	@Resource private JakdukProperties.Elasticsearch elasticsearchProperties;

	@Autowired private UrlGenerationUtils urlGenerationUtils;
	@Autowired private Client client;

	/**
	 * 통합 검색
	 *
	 * @param query	검색어
	 * @param from	페이지 시작 위치
	 * @param size	페이지 크기
	 * @return	검색 결과
	 */
	public SearchUnifiedResponse searchUnified(String query, String include, Integer from, Integer size, String preTags,
											   String postTags) {

		SearchUnifiedResponse searchUnifiedResponse = new SearchUnifiedResponse();
		Queue<Constants.SEARCH_INCLUDE_TYPE> searchOrder = new LinkedList<>();
		MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();

		if (StringUtils.contains(include, Constants.SEARCH_INCLUDE_TYPE.ARTICLE.name())) {
			SearchRequestBuilder postSearchRequestBuilder = getArticleSearchRequestBuilder(query, from, size, preTags, postTags);
			multiSearchRequestBuilder.add(postSearchRequestBuilder);
			searchOrder.offer(Constants.SEARCH_INCLUDE_TYPE.ARTICLE);
		}

		if (StringUtils.contains(include, Constants.SEARCH_INCLUDE_TYPE.COMMENT.name())) {
			SearchRequestBuilder commentSearchRequestBuilder = getCommentSearchRequestBuilder(query, from, size, preTags, postTags);
			multiSearchRequestBuilder.add(commentSearchRequestBuilder);
			searchOrder.offer(Constants.SEARCH_INCLUDE_TYPE.COMMENT);
		}

		if (StringUtils.contains(include, Constants.SEARCH_INCLUDE_TYPE.GALLERY.name())) {
			SearchRequestBuilder gallerySearchRequestBuilder = getGallerySearchRequestBuilder(query, from, size < 10 ? 4 : size);
			multiSearchRequestBuilder.add(gallerySearchRequestBuilder);
			searchOrder.offer(Constants.SEARCH_INCLUDE_TYPE.GALLERY);
		}

		MultiSearchResponse multiSearchResponse = multiSearchRequestBuilder.execute().actionGet();

		for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
			SearchResponse searchResponse = item.getResponse();
			Constants.SEARCH_INCLUDE_TYPE order = searchOrder.poll();

			if (item.isFailure())
				continue;

			if (! ObjectUtils.isEmpty(order)) {
				switch (order) {
					case ARTICLE:
						SearchBoardResult searchBoardResult = getArticleSearchResponse(searchResponse);
						searchUnifiedResponse.setPostResult(searchBoardResult);
						break;
					case COMMENT:
						SearchCommentResult searchCommentResult = getCommentSearchResponse(searchResponse);
						searchUnifiedResponse.setCommentResult(searchCommentResult);
						break;
					case GALLERY:
						SearchGalleryResult searchGalleryResult = getGallerySearchResponse(searchResponse);
						searchUnifiedResponse.setGalleryResult(searchGalleryResult);
						break;
				}
			}
		}

		return searchUnifiedResponse;
	}

	public PopularSearchWordResult aggregateSearchWord(LocalDate gteDate, Integer size) {

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(elasticsearchProperties.getIndexSearchWord())
				.setTypes(Constants.ES_TYPE_SEARCH_WORD)
				.setSize(0)
				.setQuery(
						QueryBuilders.rangeQuery("registerDate").gte(gteDate)
				)
				.addAggregation(
						AggregationBuilders
								.terms("popular_word_aggs")
								.field("word")
								.size(size)
				);

		log.debug("aggregateSearchWord Query:\n{}", searchRequestBuilder.internalBuilder());

		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		Terms popularWordTerms = searchResponse.getAggregations().get("popular_word_aggs");

		List<EsTermsBucket> popularWords = popularWordTerms.getBuckets().stream()
				.map(entry -> EsTermsBucket.builder()
						.key(entry.getKey().toString())
						.count(entry.getDocCount())
						.build())
				.collect(Collectors.toList());

		return PopularSearchWordResult.builder()
				.took(searchResponse.getTookInMillis())
				.popularSearchWords(popularWords)
				.build();
	}

	public void indexDocumentArticle(EsArticle esArticle) {

		String id = esArticle.getId();

		try {
			IndexResponse response = client.prepareIndex()
					.setIndex(elasticsearchProperties.getIndexBoard())
					.setType(Constants.ES_TYPE_ARTICLE)
					.setId(id)
					.setSource(ObjectMapperUtils.writeValueAsString(esArticle))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	public void deleteDocumentBoard(String id) {
		DeleteResponse response = client.prepareDelete()
				.setIndex(elasticsearchProperties.getIndexBoard())
				.setType(Constants.ES_TYPE_ARTICLE)
				.setId(id)
				.get();

		if (! response.isFound())
			log.info("board id {} is not found. so can't delete it!", id);
	}

	public void indexDocumentBoardComment(EsComment esComment) {

		String id = esComment.getId();
		String parentBoardId = esComment.getArticle().getId();

		try {
			IndexResponse response = client.prepareIndex()
					.setIndex(elasticsearchProperties.getIndexBoard())
					.setType(Constants.ES_TYPE_COMMENT)
					.setId(id)
					.setParent(parentBoardId)
					.setSource(ObjectMapperUtils.writeValueAsString(esComment))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	public void deleteDocumentBoardComment(String id) {

		DeleteResponse response = client.prepareDelete()
				.setIndex(elasticsearchProperties.getIndexBoard())
				.setType(Constants.ES_TYPE_COMMENT)
				.setId(id)
				.get();

		if (! response.isFound())
			log.info("comment id {} is not found. so can't delete it!", id);
	}

	// TODO : 구현 해야 함
	public void createDocumentJakduComment(EsJakduComment EsJakduComment) {}

	public void indexDocumentGallery(EsGallery esGallery) {

		String id = esGallery.getId();

		try {
			IndexResponse response = client.prepareIndex()
					.setIndex(elasticsearchProperties.getIndexGallery())
					.setType(Constants.ES_TYPE_GALLERY)
					.setId(id)
					.setSource(ObjectMapperUtils.writeValueAsString(esGallery))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	public void deleteDocumentGallery(String id) {

		DeleteResponse response = client.prepareDelete()
				.setIndex(elasticsearchProperties.getIndexGallery())
				.setType(Constants.ES_TYPE_GALLERY)
				.setId(id)
				.get();

		if (! response.isFound())
			log.info("gallery id {} is not found. so can't delete it!", id);
	}

	public void indexDocumentSearchWord(EsSearchWord esSearchWord) {

		try {
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex();

			IndexResponse response = indexRequestBuilder
					.setIndex(elasticsearchProperties.getIndexSearchWord())
					.setType(Constants.ES_TYPE_SEARCH_WORD)
					.setSource(ObjectMapperUtils.writeValueAsString(esSearchWord))
					.get();

			log.debug("indexDocumentSearchWord Source:\n {}", indexRequestBuilder.request().getDescription());

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	private SearchRequestBuilder getArticleSearchRequestBuilder(String query, Integer from, Integer size, String preTags,
																String postTags) {

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(elasticsearchProperties.getIndexBoard())
				.setTypes(Constants.ES_TYPE_ARTICLE)
				.setFetchSource(null, new String[]{"subject", "content"})
				.setQuery(
						QueryBuilders.boolQuery()
								.should(QueryBuilders.multiMatchQuery(query, "subject^1.5", "content"))
				)
				.setHighlighterNoMatchSize(Constants.SEARCH_NO_MATCH_SIZE)
				.setHighlighterFragmentSize(Constants.SEARCH_FRAGMENT_SIZE)
				.addHighlightedField("subject", Constants.SEARCH_FRAGMENT_SIZE, 0)
				.addHighlightedField("content", Constants.SEARCH_FRAGMENT_SIZE, 1)
				.setFrom(from)
				.setSize(size);

		if (StringUtils.isNotBlank(preTags))
			searchRequestBuilder.setHighlighterPreTags(preTags);

		if (StringUtils.isNotBlank(postTags))
			searchRequestBuilder.setHighlighterPostTags(postTags);

		log.debug("getArticleSearchRequestBuilder Query:\n{}", searchRequestBuilder.internalBuilder());

		return searchRequestBuilder;
	}

	private SearchBoardResult getArticleSearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<ArticleSource> searchList = Arrays.stream(searchHits.getHits())
				.map(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					EsArticleSource esArticleSource = ObjectMapperUtils.convertValue(sourceMap, EsArticleSource.class);
					esArticleSource.setScore(searchHit.getScore());

					Map<String, List<String>> highlight = this.getHighlight(searchHit.getHighlightFields().entrySet());
					esArticleSource.setHighlight(highlight);

					return esArticleSource;
				})
				.map(esArticleSource -> {
					ArticleSource articleSource = new ArticleSource();
					BeanUtils.copyProperties(esArticleSource, articleSource);

					if (! ObjectUtils.isEmpty(esArticleSource.getGalleries())) {
						List<BoardGallerySimple> boardGalleries = esArticleSource.getGalleries().stream()
								.sorted(Comparator.comparing(String::toString))
								.limit(1)
								.map(galleryId -> BoardGallerySimple.builder()
										.id(galleryId)
										.thumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, galleryId))
										.build())
								.collect(Collectors.toList());

						articleSource.setGalleries(boardGalleries);
					}

					return articleSource;
				})
				.collect(Collectors.toList());

		return SearchBoardResult.builder()
				.took(searchResponse.getTook().getMillis())
				.totalCount(searchHits.getTotalHits())
				.articles(searchList)
				.build();
	}

	private SearchRequestBuilder getCommentSearchRequestBuilder(String query, Integer from, Integer size, String preTags,
																String postTags) {

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(elasticsearchProperties.getIndexBoard())
				.setTypes(Constants.ES_TYPE_COMMENT)
				.setFetchSource(null, new String[]{"content"})
				.setQuery(
						QueryBuilders.boolQuery()
								.must(QueryBuilders.matchQuery("content", query))
								.must(
										QueryBuilders
												.hasParentQuery(Constants.ES_TYPE_ARTICLE, QueryBuilders.matchAllQuery())
												.innerHit(new QueryInnerHitBuilder())
								)
				)
				.setHighlighterNoMatchSize(Constants.SEARCH_NO_MATCH_SIZE)
				.setHighlighterFragmentSize(Constants.SEARCH_FRAGMENT_SIZE)
				.addHighlightedField("content", Constants.SEARCH_FRAGMENT_SIZE, 1)
				.setFrom(from)
				.setSize(size);

		if (StringUtils.isNotBlank(preTags))
			searchRequestBuilder.setHighlighterPreTags(preTags);

		if (StringUtils.isNotBlank(postTags))
			searchRequestBuilder.setHighlighterPostTags(postTags);

		log.debug("getBoardCommentSearchRequestBuilder Query:\n{}", searchRequestBuilder.internalBuilder());

		return searchRequestBuilder;
	}

	private SearchCommentResult getCommentSearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<EsCommentSource> searchList = Arrays.stream(searchHits.getHits())
				.map(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					EsCommentSource esCommentSource = ObjectMapperUtils.convertValue(sourceMap, EsCommentSource.class);
					esCommentSource.setScore(searchHit.getScore());

					if (! searchHit.getInnerHits().isEmpty()) {
						SearchHit[] innerSearchHits = searchHit.getInnerHits().get(Constants.ES_TYPE_ARTICLE).getHits();
						Map<String, Object> innerSourceMap = innerSearchHits[ innerSearchHits.length - 1 ].getSource();
						EsParentArticle esParentArticle = ObjectMapperUtils.convertValue(innerSourceMap, EsParentArticle.class);

						esCommentSource.setParentArticle(esParentArticle);
					}

					Map<String, List<String>> highlight = this.getHighlight(searchHit.getHighlightFields().entrySet());
					esCommentSource.setHighlight(highlight);

					return esCommentSource;
				})
				.collect(Collectors.toList());

		return SearchCommentResult.builder()
				.took(searchResponse.getTook().getMillis())
				.totalCount(searchHits.getTotalHits())
				.comments(searchList)
				.build();
	}

	private SearchRequestBuilder getGallerySearchRequestBuilder(String query, Integer from, Integer size) {
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(elasticsearchProperties.getIndexGallery())
				.setTypes(Constants.ES_TYPE_GALLERY)
				.setFetchSource(null, new String[]{"name"})
				.setQuery(QueryBuilders.matchQuery("name", query))
				.setHighlighterNoMatchSize(Constants.SEARCH_NO_MATCH_SIZE)
				.setHighlighterFragmentSize(Constants.SEARCH_FRAGMENT_SIZE)
				.setHighlighterPreTags("<span class=\"color-orange\">")
				.setHighlighterPostTags("</span>")
				.addHighlightedField("name", Constants.SEARCH_FRAGMENT_SIZE, 0)
				.setFrom(from)
				.setSize(size);

		log.debug("getGallerySearchRequestBuilder Query:\n{}", searchRequestBuilder.internalBuilder());

		return searchRequestBuilder;
	}

	private SearchGalleryResult getGallerySearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<EsGallerySource> searchList = Arrays.stream(searchHits.getHits())
				.map(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					EsGallerySource esGallerySource = ObjectMapperUtils.convertValue(sourceMap, EsGallerySource.class);
					esGallerySource.setScore(searchHit.getScore());

					Map<String, List<String>> highlight = this.getHighlight(searchHit.getHighlightFields().entrySet());
					esGallerySource.setHighlight(highlight);

					return esGallerySource;
				})
				.collect(Collectors.toList());

		return SearchGalleryResult.builder()
				.took(searchResponse.getTook().getMillis())
				.totalCount(searchHits.getTotalHits())
				.galleries(searchList)
				.build();
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

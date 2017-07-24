package com.jakduk.api.service;

import com.jakduk.api.common.CoreConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.configuration.CoreProperties;
import com.jakduk.api.model.elasticsearch.*;
import com.jakduk.api.vo.board.BoardGallerySimple;
import com.jakduk.api.vo.search.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
*/

@Slf4j
@Service
public class SearchService {

	@Resource
	private CoreProperties coreProperties;

	@Resource
	private ApiUtils apiUtils;

	@Autowired
	private Client client;

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
		Queue<CoreConst.SEARCH_TYPE> searchOrder = new LinkedList<>();
		MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();

		if (StringUtils.contains(include, CoreConst.SEARCH_TYPE.PO.name())) {
			SearchRequestBuilder postSearchRequestBuilder = getBoardSearchRequestBuilder(query, from, size, preTags, postTags);
			multiSearchRequestBuilder.add(postSearchRequestBuilder);
			searchOrder.offer(CoreConst.SEARCH_TYPE.PO);
		}

		if (StringUtils.contains(include, CoreConst.SEARCH_TYPE.CO.name())) {
			SearchRequestBuilder commentSearchRequestBuilder = getCommentSearchRequestBuilder(query, from, size, preTags, postTags);
			multiSearchRequestBuilder.add(commentSearchRequestBuilder);
			searchOrder.offer(CoreConst.SEARCH_TYPE.CO);
		}

		if (StringUtils.contains(include, CoreConst.SEARCH_TYPE.GA.name())) {
			SearchRequestBuilder gallerySearchRequestBuilder = getGallerySearchRequestBuilder(query, from, size < 10 ? 4 : size);
			multiSearchRequestBuilder.add(gallerySearchRequestBuilder);
			searchOrder.offer(CoreConst.SEARCH_TYPE.GA);
		}

		MultiSearchResponse multiSearchResponse = multiSearchRequestBuilder.execute().actionGet();

		for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
			SearchResponse searchResponse = item.getResponse();
			CoreConst.SEARCH_TYPE order = searchOrder.poll();

			if (item.isFailure())
				continue;

			if (! ObjectUtils.isEmpty(order)) {
				switch (order) {
					case PO:
						SearchBoardResult searchBoardResult = getBoardSearchResponse(searchResponse);
						searchUnifiedResponse.setPostResult(searchBoardResult);
						break;
					case CO:
						SearchCommentResult searchCommentResult = getCommentSearchResponse(searchResponse);
						searchUnifiedResponse.setCommentResult(searchCommentResult);
						break;
					case GA:
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
				.setIndices(coreProperties.getElasticsearch().getIndexSearchWord())
				.setTypes(CoreConst.ES_TYPE_SEARCH_WORD)
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

	private SearchRequestBuilder getBoardSearchRequestBuilder(String query, Integer from, Integer size, String preTags,
															  String postTags) {

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(coreProperties.getElasticsearch().getIndexBoard())
				.setTypes(CoreConst.ES_TYPE_BOARD)
				.setFetchSource(null, new String[]{"subject", "content"})
				.setQuery(
						QueryBuilders.boolQuery()
								.should(QueryBuilders.multiMatchQuery(query, "subject^1.5", "content"))
				)
				.setHighlighterNoMatchSize(CoreConst.SEARCH_NO_MATCH_SIZE)
				.setHighlighterFragmentSize(CoreConst.SEARCH_FRAGMENT_SIZE)
				.addHighlightedField("subject", CoreConst.SEARCH_FRAGMENT_SIZE, 0)
				.addHighlightedField("content", CoreConst.SEARCH_FRAGMENT_SIZE, 1)
				.setFrom(from)
				.setSize(size);

		if (StringUtils.isNotBlank(preTags))
			searchRequestBuilder.setHighlighterPreTags(preTags);

		if (StringUtils.isNotBlank(postTags))
			searchRequestBuilder.setHighlighterPostTags(postTags);

		log.debug("getBoardSearchRequestBuilder Query:\n{}", searchRequestBuilder.internalBuilder());

		return searchRequestBuilder;
	}

	private SearchBoardResult getBoardSearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<BoardSource> searchList = Arrays.stream(searchHits.getHits())
				.map(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					EsBoardSource esBoardSource = ObjectMapperUtils.convertValue(sourceMap, EsBoardSource.class);
					esBoardSource.setScore(searchHit.getScore());

					Map<String, List<String>> highlight = this.getHighlight(searchHit.getHighlightFields().entrySet());
					esBoardSource.setHighlight(highlight);

					return esBoardSource;
				})
				.map(esBoardSource -> {
					BoardSource boardSource = new BoardSource();
					BeanUtils.copyProperties(esBoardSource, boardSource);

					if (! ObjectUtils.isEmpty(esBoardSource.getGalleries())) {
						List<BoardGallerySimple> boardGalleries = esBoardSource.getGalleries().stream()
								.sorted(Comparator.comparing(String::toString))
								.limit(1)
								.map(galleryId -> BoardGallerySimple.builder()
										.id(galleryId)
										.thumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, galleryId))
										.build())
								.collect(Collectors.toList());

						boardSource.setGalleries(boardGalleries);
					}

					return boardSource;
				})
				.collect(Collectors.toList());

		return SearchBoardResult.builder()
				.took(searchResponse.getTook().getMillis())
				.totalCount(searchHits.getTotalHits())
				.posts(searchList)
				.build();
	}

	private SearchRequestBuilder getCommentSearchRequestBuilder(String query, Integer from, Integer size, String preTags,
																String postTags) {

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(coreProperties.getElasticsearch().getIndexBoard())
				.setTypes(CoreConst.ES_TYPE_COMMENT)
				.setFetchSource(null, new String[]{"content"})
				.setQuery(
						QueryBuilders.boolQuery()
								.must(QueryBuilders.matchQuery("content", query))
								.must(
										QueryBuilders
												.hasParentQuery(CoreConst.ES_TYPE_BOARD, QueryBuilders.matchAllQuery())
												.innerHit(new QueryInnerHitBuilder())
								)
				)
				.setHighlighterNoMatchSize(CoreConst.SEARCH_NO_MATCH_SIZE)
				.setHighlighterFragmentSize(CoreConst.SEARCH_FRAGMENT_SIZE)
				.addHighlightedField("content", CoreConst.SEARCH_FRAGMENT_SIZE, 1)
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
						SearchHit[] innerSearchHits = searchHit.getInnerHits().get(CoreConst.ES_TYPE_BOARD).getHits();
						Map<String, Object> innerSourceMap = innerSearchHits[ innerSearchHits.length - 1 ].getSource();
						EsParentBoard esParentBoard = ObjectMapperUtils.convertValue(innerSourceMap, EsParentBoard.class);

						esCommentSource.setParentBoard(esParentBoard);
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
				.setIndices(coreProperties.getElasticsearch().getIndexGallery())
				.setTypes(CoreConst.ES_TYPE_GALLERY)
				.setFetchSource(null, new String[]{"name"})
				.setQuery(QueryBuilders.matchQuery("name", query))
				.setHighlighterNoMatchSize(CoreConst.SEARCH_NO_MATCH_SIZE)
				.setHighlighterFragmentSize(CoreConst.SEARCH_FRAGMENT_SIZE)
				.setHighlighterPreTags("<span class=\"color-orange\">")
				.setHighlighterPostTags("</span>")
				.addHighlightedField("name", CoreConst.SEARCH_FRAGMENT_SIZE, 0)
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

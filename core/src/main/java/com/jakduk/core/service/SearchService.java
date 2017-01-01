package com.jakduk.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.common.util.ObjectMapperUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.elasticsearch.*;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.vo.*;
import com.jakduk.core.repository.board.free.BoardFreeCommentRepository;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.jakduk.core.repository.gallery.GalleryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
*/

@Slf4j
@Service
public class SearchService {
	
    @Value("${elasticsearch.enable}")
    private boolean elasticsearchEnable;

	@Value("${core.elasticsearch.index.board}")
	private String elasticsearchIndexBoard;

	@Value("${core.elasticsearch.index.gallery}")
	private String elasticsearchIndexGallery;

	@Value("${core.elasticsearch.index.search.word}")
	private String elasticsearchIndexSearchWord;

	@Value("${core.elasticsearch.bulk.actions}")
	private Integer bulkActions;

	@Value("${core.elasticsearch.bulk.size.mb}")
	private Integer bulkMbSize;

	@Value("${core.elasticsearch.bulk.flush.interval.seconds}")
	private Integer bulkFlushIntervalSeconds;

	@Value("${core.elasticsearch.bulk.concurrent.requests}")
	private Integer bulkConcurrentRequests;

	@Autowired
	private Client client;

	@Autowired
	private BoardFreeRepository boardFreeRepository;

	@Autowired
	private BoardFreeCommentRepository boardFreeCommentRepository;

	@Autowired
	private GalleryRepository galleryRepository;

	/**
	 * 통합 검색
	 * @param query	검색어
	 * @param from	페이지 시작 위치
	 * @param size	페이지 크기
	 * @return	검색 결과
	 */
	public SearchUnifiedResponse searchUnified(String query, String include, Integer from, Integer size) {

		SearchUnifiedResponse searchUnifiedResponse = new SearchUnifiedResponse();
		Queue<CoreConst.SEARCH_TYPE> searchOrder = new LinkedList<>();
		MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();

		if (StringUtils.contains(include, CoreConst.SEARCH_TYPE.PO.name())) {
			SearchRequestBuilder postSearchRequestBuilder = getBoardSearchRequestBuilder(query, from, size);
			multiSearchRequestBuilder.add(postSearchRequestBuilder);
			searchOrder.offer(CoreConst.SEARCH_TYPE.PO);
		}

		if (StringUtils.contains(include, CoreConst.SEARCH_TYPE.CO.name())) {
			SearchRequestBuilder commentSearchRequestBuilder = getCommentSearchRequestBuilder(query, from, size);
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

	@Async
	public void indexDocumentBoard(String id, Integer seq, CommonWriter writer, String subject, String content, String category) {

		if (! elasticsearchEnable)
			return;

		ESBoard esBoard = ESBoard.builder()
				.id(id)
				.seq(seq)
				.writer(writer)
				.subject(CoreUtils.stripHtmlTag(subject))
				.content(CoreUtils.stripHtmlTag(content))
				.category(category)
				.build();

		try {
			IndexResponse response = client.prepareIndex()
					.setIndex(elasticsearchIndexBoard)
					.setType(CoreConst.ES_TYPE_BOARD)
					.setId(id)
					.setSource(ObjectMapperUtils.writeValueAsString(esBoard))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	@Async
	public void deleteDocumentBoard(String id) {

		if (! elasticsearchEnable)
			return;

		DeleteResponse response = client.prepareDelete()
				.setIndex(elasticsearchIndexBoard)
				.setType(CoreConst.ES_TYPE_BOARD)
				.setId(id)
				.get();

		if (! response.isFound())
			log.info("board id " + id + " is not found. so can't delete it!");

	}

	@Async
	public void indexDocumentBoardComment(String id, BoardItem boardItem, CommonWriter writer, String content) {

		if (! elasticsearchEnable)
			return;

		ESComment esComment = ESComment.builder()
				.id(id)
				.boardItem(boardItem)
				.writer(writer)
				.content(CoreUtils.stripHtmlTag(content))
				.build();

		try {
			IndexResponse response = client.prepareIndex()
					.setIndex(elasticsearchIndexBoard)
					.setType(CoreConst.ES_TYPE_COMMENT)
					.setId(id)
					.setParent(boardItem.getId())
					.setSource(ObjectMapperUtils.writeValueAsString(esComment))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	// TODO : 구현 해야 함
	public void createDocumentJakduComment(ESJakduComment ESJakduComment) {}

	@Async
	public void indexDocumentGallery(String id, CommonWriter writer, String name) {

		if (! elasticsearchEnable)
			return;

		ESGallery esGallery = ESGallery.builder()
				.id(id)
				.writer(writer)
				.name(name)
				.build();

		try {
			IndexResponse response = client.prepareIndex()
					.setIndex(elasticsearchIndexGallery)
					.setType(CoreConst.ES_TYPE_GALLERY)
					.setId(id)
					.setSource(ObjectMapperUtils.writeValueAsString(esGallery))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	@Async
	public void deleteDocumentGallery(String id) {

		if (! elasticsearchEnable)
			return;

		DeleteResponse response = client.prepareDelete()
				.setIndex(elasticsearchIndexGallery)
				.setType(CoreConst.ES_TYPE_GALLERY)
				.setId(id)
				.get();

		if (! response.isFound())
			log.info("gallery id " + id + " is not found. so can't delete it!");
	}

	@Async
	public void indexDocumentSearchWord(String word, CommonWriter writer) {

		if (! elasticsearchEnable)
			return;

		ESSearchWord esSearchWord = ESSearchWord.builder()
				.word(word)
				.writer(writer)
				.registerDate(LocalDateTime.now())
				.build();

		try {
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex();

			IndexResponse response = indexRequestBuilder
					.setIndex(elasticsearchIndexSearchWord)
					.setType(CoreConst.ES_TYPE_SEARCH_WORD)
					.setSource(ObjectMapperUtils.writeValueAsString(esSearchWord))
					.get();

			log.debug("indexDocumentSearchWord Source:\n" + indexRequestBuilder.request().getDescription());

		} catch (IOException e) {
			throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	public PopularSearchWordResult aggregateSearchWord(Long registerDateFrom, Integer size) {

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(elasticsearchIndexSearchWord)
				.setTypes(CoreConst.ES_TYPE_SEARCH_WORD)
				.setSize(0)
				.setQuery(
						QueryBuilders.rangeQuery("registerDate").gte(registerDateFrom)
				)
				.addAggregation(
						AggregationBuilders
								.terms("popular_word_aggs")
								.field("word")
								.size(size)
				);

		log.debug("aggregateSearchWord Query:\n" + searchRequestBuilder.internalBuilder());

		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		Terms popularWordTerms = searchResponse.getAggregations().get("popular_word_aggs");

		List<ESTermsBucket> popularWords = popularWordTerms.getBuckets().stream()
				.map(entry -> ESTermsBucket.builder()
						.key(entry.getKey().toString())
						.count(entry.getDocCount())
						.build())
				.collect(Collectors.toList());

		return PopularSearchWordResult.builder()
				.took(searchResponse.getTookInMillis())
				.popularSearchWords(popularWords)
				.build();
	}

	public void createIndexBoard() {

		String index = elasticsearchIndexBoard;

		try {
			CreateIndexResponse	response = client.admin().indices().prepareCreate(index)
                    .setSettings(getIndexSettings())
                    .addMapping(CoreConst.ES_TYPE_BOARD, getBoardFreeMappings())
					.addMapping(CoreConst.ES_TYPE_COMMENT, getBoardFreeCommentMappings())
                    .get();

			if (response.isAcknowledged()) {
				log.debug("Index " + index + " created");
			} else {
				throw new RuntimeException("Index " + index + " not created");
			}

		} catch (JsonProcessingException e) {
			throw new RuntimeException("Index " + index + " not created", e.getCause());
		}
	}

	public void createIndexGallery() {

		String index = elasticsearchIndexGallery;

		try {
			CreateIndexResponse response = client.admin().indices().prepareCreate(index)
                    .setSettings(getIndexSettings())
                    .addMapping(CoreConst.ES_TYPE_GALLERY, getGalleryMappings())
                    .get();

			if (response.isAcknowledged()) {
				log.debug("Index " + index + " created");
			} else {
				throw new RuntimeException("Index " + index + " not created");
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Index " + index + " not created", e.getCause());
		}
	}

	public void createIndexSearchWord() {

		String index = elasticsearchIndexSearchWord;

		try {
			CreateIndexResponse response = client.admin().indices().prepareCreate(index)
					.setSettings(getIndexSettings())
					.addMapping(CoreConst.ES_TYPE_SEARCH_WORD, getSearchWordMappings())
					.get();

			if (response.isAcknowledged()) {
				log.debug("Index " + index + " created");
			} else {
				throw new RuntimeException("Index " + index + " not created");
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Index " + index + " not created", e.getCause());
		}
	}

	public void processBulkInsertBoard() throws InterruptedException {

		BulkProcessor bulkProcessor = getBulkProcessor();

		Boolean hasPost = true;
		ObjectId lastPostId = null;

		do {
			List<ESBoard> posts = boardFreeRepository.findPostsGreaterThanId(lastPostId, CoreConst.ES_BULK_LIMIT);

			if (posts.isEmpty()) {
				hasPost = false;
			} else {
				ESBoard lastPost = posts.get(posts.size() - 1);
				lastPostId = new ObjectId(lastPost.getId());
			}

			posts.forEach(post -> {
				IndexRequestBuilder index = client.prepareIndex(
						elasticsearchIndexBoard,
						CoreConst.ES_TYPE_BOARD,
						post.getId()
				);

				try {
					index.setSource(ObjectMapperUtils.writeValueAsString(post));
					bulkProcessor.add(index.request());

				} catch (JsonProcessingException e) {
					log.error(e.getLocalizedMessage());
				}

			});

		} while (hasPost);

		bulkProcessor.awaitClose(CoreConst.ES_AWAIT_CLOSE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
	}

	public void processBulkInsertComment() throws InterruptedException {

		BulkProcessor bulkProcessor = getBulkProcessor();

		Boolean hasComment = true;
		ObjectId lastCommentId = null;

		do {
			List<ESComment> comments = boardFreeCommentRepository.findCommentsGreaterThanId(lastCommentId, CoreConst.ES_BULK_LIMIT);

			if (comments.isEmpty()) {
				hasComment = false;
			} else {
				ESComment lastComment = comments.get(comments.size() - 1);
				lastCommentId = new ObjectId(lastComment.getId());
			}

			comments.forEach(comment -> {
				try {
					IndexRequestBuilder index = client.prepareIndex()
							.setIndex(elasticsearchIndexBoard)
							.setType(CoreConst.ES_TYPE_COMMENT)
							.setId(comment.getId())
							.setParent(comment.getBoardItem().getId())
							.setSource(ObjectMapperUtils.writeValueAsString(comment));

					bulkProcessor.add(index.request());

				} catch (JsonProcessingException e) {
					log.error(e.getLocalizedMessage());
				}

			});

		} while (hasComment);

		bulkProcessor.awaitClose(CoreConst.ES_AWAIT_CLOSE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
	}

	public void processBulkInsertGallery() throws InterruptedException {

		BulkProcessor bulkProcessor = getBulkProcessor();

		Boolean hasGallery = true;
		ObjectId lastGalleryId = null;

		do {
			List<ESGallery> comments = galleryRepository.findGalleriesGreaterThanId(lastGalleryId, CoreConst.ES_BULK_LIMIT);

			if (comments.isEmpty()) {
				hasGallery = false;
			} else {
				ESGallery lastGallery = comments.get(comments.size() - 1);
				lastGalleryId = new ObjectId(lastGallery.getId());
			}

			comments.forEach(comment -> {
				IndexRequestBuilder index = client.prepareIndex(
						elasticsearchIndexGallery,
						CoreConst.ES_TYPE_GALLERY,
						comment.getId()
				);

				try {
					index.setSource(ObjectMapperUtils.writeValueAsString(comment));
					bulkProcessor.add(index.request());

				} catch (JsonProcessingException e) {
					log.error(e.getLocalizedMessage());
				}

			});

		} while (hasGallery);

		bulkProcessor.awaitClose(CoreConst.ES_AWAIT_CLOSE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
	}

	public void deleteIndexBoard() {

		String index = elasticsearchIndexBoard;

		DeleteIndexResponse response = client.admin().indices()
				.delete(new DeleteIndexRequest(index))
				.actionGet();

		if (response.isAcknowledged()) {
			log.debug("Index " + index + " deleted");
		} else {
			throw new RuntimeException("Index " + index + " not deleted");
		}
	}

	public void deleteIndexGallery() {

		String index = elasticsearchIndexGallery;

		DeleteIndexResponse response = client.admin().indices()
				.delete(new DeleteIndexRequest(index))
				.actionGet();

		if (response.isAcknowledged()) {
			log.debug("Index " + index + " deleted");
		} else {
			throw new RuntimeException("Index " + index + " not deleted");
		}
	}

	public void deleteIndexSearchWord() {

		String index = elasticsearchIndexSearchWord;

		DeleteIndexResponse response = client.admin().indices()
				.delete(new DeleteIndexRequest(index))
				.actionGet();

		if (response.isAcknowledged()) {
			log.debug("Index " + index + " deleted");
		} else {
			throw new RuntimeException("Index " + index + " not deleted");
		}
	}

	private SearchRequestBuilder getBoardSearchRequestBuilder(String query, Integer from, Integer size) {
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(elasticsearchIndexBoard)
				.setTypes(CoreConst.ES_TYPE_BOARD)
				.setFetchSource(null, new String[]{"subject", "content"})
				.setQuery(
						QueryBuilders.boolQuery()
								.should(QueryBuilders.multiMatchQuery(query, "subject^1.5", "content"))
				)
				.setHighlighterNoMatchSize(CoreConst.SEARCH_NO_MATCH_SIZE)
				.setHighlighterFragmentSize(CoreConst.SEARCH_FRAGMENT_SIZE)
				.setHighlighterPreTags("<span class=\"color-orange\">")
				.setHighlighterPostTags("</span>")
				.addHighlightedField("subject", CoreConst.SEARCH_FRAGMENT_SIZE, 0)
				.addHighlightedField("content", CoreConst.SEARCH_FRAGMENT_SIZE, 1)
				.setFrom(from)
				.setSize(size);

		log.debug("getBoardSearchRequestBuilder Query:\n" + searchRequestBuilder.internalBuilder());

		return searchRequestBuilder;
	}

	private SearchBoardResult getBoardSearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<ESBoardSource> searchList = Arrays.stream(searchHits.getHits())
				.map(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					ESBoardSource esBoardSource = ObjectMapperUtils.convertValue(sourceMap, ESBoardSource.class);
					esBoardSource.setScore(searchHit.getScore());

					Map<String, List<String>> highlight = new HashMap<>();

					for (Map.Entry<String, HighlightField> highlightField : searchHit.getHighlightFields().entrySet()) {
						List<String> fragments = new ArrayList<>();
						for (Text text : highlightField.getValue().fragments()) {
							fragments.add(text.string());
						}
						highlight.put(highlightField.getKey(), fragments);
					}

					esBoardSource.setHighlight(highlight);

					return esBoardSource;
				})
				.collect(Collectors.toList());

		return SearchBoardResult.builder()
				.took(searchResponse.getTook().getMillis())
				.totalCount(searchHits.getTotalHits())
				.posts(searchList)
				.build();
	}

	private SearchRequestBuilder getCommentSearchRequestBuilder(String query, Integer from, Integer size) {
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(elasticsearchIndexBoard)
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
				.setHighlighterPreTags("<span class=\"color-orange\">")
				.setHighlighterPostTags("</span>")
				.addHighlightedField("content", CoreConst.SEARCH_FRAGMENT_SIZE, 1)
				.setFrom(from)
				.setSize(size);

		log.debug("getBoardCommentSearchRequestBuilder Query:\n" + searchRequestBuilder.internalBuilder());

		return searchRequestBuilder;
	}

	private SearchCommentResult getCommentSearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<ESCommentSource> searchList = Arrays.stream(searchHits.getHits())
				.map(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					ESCommentSource esCommentSource = ObjectMapperUtils.convertValue(sourceMap, ESCommentSource.class);
					esCommentSource.setScore(searchHit.getScore());

					if (! searchHit.getInnerHits().isEmpty()) {
						SearchHit[] innerSearchHits = searchHit.getInnerHits().get(CoreConst.ES_TYPE_BOARD).getHits();
						Map<String, Object> innerSourceMap = innerSearchHits[ innerSearchHits.length - 1 ].getSource();
						ESParentBoard esParentBoard = ObjectMapperUtils.convertValue(innerSourceMap, ESParentBoard.class);

						esCommentSource.setParentBoard(esParentBoard);
					}

					Map<String, List<String>> highlight = new HashMap<>();

					for (Map.Entry<String, HighlightField> highlightField : searchHit.getHighlightFields().entrySet()) {
						List<String> fragments = new ArrayList<>();
						for (Text text : highlightField.getValue().fragments()) {
							fragments.add(text.string());
						}
						highlight.put(highlightField.getKey(), fragments);
					}

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
				.setIndices(elasticsearchIndexGallery)
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

		log.debug("getGallerySearchRequestBuilder Query:\n" + searchRequestBuilder.internalBuilder());

		return searchRequestBuilder;
	}

	private SearchGalleryResult getGallerySearchResponse(SearchResponse searchResponse) {
		SearchHits searchHits = searchResponse.getHits();

		List<ESGallerySource> searchList = Arrays.stream(searchHits.getHits())
				.map(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					ESGallerySource esGallerySource = ObjectMapperUtils.convertValue(sourceMap, ESGallerySource.class);
					esGallerySource.setScore(searchHit.getScore());

					Map<String, List<String>> highlight = new HashMap<>();

					for (Map.Entry<String, HighlightField> highlightField : searchHit.getHighlightFields().entrySet()) {
						List<String> fragments = new ArrayList<>();
						for (Text text : highlightField.getValue().fragments()) {
							fragments.add(text.string());
						}
						highlight.put(highlightField.getKey(), fragments);
					}

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

	private BulkProcessor getBulkProcessor() {
		BulkProcessor.Listener bulkProcessorListener = new BulkProcessor.Listener() {
			@Override public void beforeBulk(long l, BulkRequest bulkRequest) {
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
				log.debug("bulk took:" + bulkResponse.getTookInMillis());
				if (bulkResponse.hasFailures())
					log.error(bulkResponse.buildFailureMessage());
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
				log.error(throwable.getLocalizedMessage());
			}
		};

		return BulkProcessor.builder(client, bulkProcessorListener)
				.setBulkActions(bulkActions)
				.setBulkSize(new ByteSizeValue(bulkMbSize, ByteSizeUnit.MB))
				.setFlushInterval(TimeValue.timeValueSeconds(bulkFlushIntervalSeconds))
				.setConcurrentRequests(bulkConcurrentRequests)
				.build();
	}


	private Settings.Builder getIndexSettings() {

		//settingsBuilder.put("number_of_shards", 5);
		//settingsBuilder.put("number_of_replicas", 1);

		String[] userWords = new String[]{
				"k리그",
				"내셔널리그",
				"k3리그",
				"k3",
				"성남fc",
				"수원fc",
				"인천utd",
				"인천유나이티드",
				"강원fc",
				"fc서울",
				"fc안양",
				"부천fc",
				"대구fc",
				"광주fc",
				"경남fc",
				"제주utd",
				"제주유나이티드",
				"서울e"
		};

		return Settings.builder()
				.put("index.analysis.analyzer.korean.type", "custom")
				.put("index.analysis.analyzer.korean.tokenizer", "seunjeon_default_tokenizer")
                .putArray("index.analysis.tokenizer.seunjeon_default_tokenizer.user_words", userWords)
				.put("index.analysis.tokenizer.seunjeon_default_tokenizer.type", "seunjeon_tokenizer")
				.put("index.analysis.tokenizer.seunjeon_default_tokenizer.pos_tagging", false)
				.put("index.analysis.tokenizer.seunjeon_default_tokenizer.decompound", true)
				.putArray("index.analysis.tokenizer.seunjeon_default_tokenizer.index_poses",
						"N", "SL", "SH", "SN", "XR", "V", "UNK", "I", "M");
	}

	private String getBoardFreeMappings() throws JsonProcessingException {
		ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

		ObjectNode idNode = objectMapper.createObjectNode();
		idNode.put("type", "string");

		ObjectNode subjectNode = objectMapper.createObjectNode();
		subjectNode.put("type", "string");
		subjectNode.put("analyzer", "korean");

		ObjectNode contentNode = objectMapper.createObjectNode();
		contentNode.put("type", "string");
		contentNode.put("analyzer", "korean");

		ObjectNode seqNode = objectMapper.createObjectNode();
		seqNode.put("type", "integer");
		seqNode.put("index", "no");

		ObjectNode categoryNode = objectMapper.createObjectNode();
		categoryNode.put("type", "string");
		categoryNode.put("index", "not_analyzed");

		// writer
		ObjectNode writerProviderIdNode = objectMapper.createObjectNode();
		writerProviderIdNode.put("type", "string");
		writerProviderIdNode.put("index", "no");

		ObjectNode writerUserIdNode = objectMapper.createObjectNode();
		writerUserIdNode.put("type", "string");
		writerUserIdNode.put("index", "no");

		ObjectNode writerUsernameNode = objectMapper.createObjectNode();
		writerUsernameNode.put("type", "string");
		writerUsernameNode.put("index", "no");

		ObjectNode writerPropertiesNode = objectMapper.createObjectNode();
		writerPropertiesNode.set("providerId", writerProviderIdNode);
		writerPropertiesNode.set("userId", writerUserIdNode);
		writerPropertiesNode.set("username", writerUsernameNode);

		ObjectNode writerNode = objectMapper.createObjectNode();
		writerNode.set("properties", writerPropertiesNode);

		// properties
		ObjectNode propertiesNode = objectMapper.createObjectNode();
		propertiesNode.set("id", idNode);
		propertiesNode.set("subject", subjectNode);
		propertiesNode.set("content", contentNode);
		propertiesNode.set("seq", seqNode);
		propertiesNode.set("writer", writerNode);
		propertiesNode.set("category", categoryNode);

		ObjectNode mappings = objectMapper.createObjectNode();
		mappings.set("properties", propertiesNode);

		return objectMapper.writeValueAsString(mappings);
	}

	private String getBoardFreeCommentMappings() throws JsonProcessingException {
		ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

		ObjectNode idNode = objectMapper.createObjectNode();
		idNode.put("type", "string");

		ObjectNode contentNode = objectMapper.createObjectNode();
		contentNode.put("type", "string");
		contentNode.put("analyzer", "korean");

		// boardItem
		ObjectNode boardItemIdNode = objectMapper.createObjectNode();
		boardItemIdNode.put("type", "string");
		boardItemIdNode.put("index", "no");

		ObjectNode boardItemSeqNode = objectMapper.createObjectNode();
		boardItemSeqNode.put("type", "integer");
		boardItemSeqNode.put("index", "no");

		ObjectNode boardItemPropertiesNode = objectMapper.createObjectNode();
		boardItemPropertiesNode.set("id", boardItemIdNode);
		boardItemPropertiesNode.set("seq", boardItemSeqNode);

		ObjectNode boardItemNode = objectMapper.createObjectNode();
		boardItemNode.set("properties", boardItemPropertiesNode);

		// writer
		ObjectNode writerProviderIdNode = objectMapper.createObjectNode();
		writerProviderIdNode.put("type", "string");
		writerProviderIdNode.put("index", "no");

		ObjectNode writerUserIdNode = objectMapper.createObjectNode();
		writerUserIdNode.put("type", "string");
		writerUserIdNode.put("index", "no");

		ObjectNode writerUsernameNode = objectMapper.createObjectNode();
		writerUsernameNode.put("type", "string");
		writerUsernameNode.put("index", "no");

		ObjectNode writerPropertiesNode = objectMapper.createObjectNode();
		writerPropertiesNode.set("providerId", writerProviderIdNode);
		writerPropertiesNode.set("userId", writerUserIdNode);
		writerPropertiesNode.set("username", writerUsernameNode);

		ObjectNode writerNode = objectMapper.createObjectNode();
		writerNode.set("properties", writerPropertiesNode);

		// properties
		ObjectNode propertiesNode = objectMapper.createObjectNode();
		propertiesNode.set("id", idNode);
		propertiesNode.set("content", contentNode);
		propertiesNode.set("writer", writerNode);
		propertiesNode.set("boardItem", boardItemNode);

		ObjectNode parentNode = objectMapper.createObjectNode();
		parentNode.put("type", CoreConst.ES_TYPE_BOARD);

		ObjectNode mappings = objectMapper.createObjectNode();
		mappings.set("_parent", parentNode);
		mappings.set("properties", propertiesNode);

		return objectMapper.writeValueAsString(mappings);
	}

	private String getGalleryMappings() throws JsonProcessingException {
		ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

		ObjectNode idNode = objectMapper.createObjectNode();
		idNode.put("type", "string");

		ObjectNode nameNode = objectMapper.createObjectNode();
		nameNode.put("type", "string");
		nameNode.put("analyzer", "korean");

		// writer
		ObjectNode writerProviderIdNode = objectMapper.createObjectNode();
		writerProviderIdNode.put("type", "string");
		writerProviderIdNode.put("index", "no");

		ObjectNode writerUserIdNode = objectMapper.createObjectNode();
		writerUserIdNode.put("type", "string");
		writerUserIdNode.put("index", "no");

		ObjectNode writerUsernameNode = objectMapper.createObjectNode();
		writerUsernameNode.put("type", "string");
		writerUsernameNode.put("index", "no");

		ObjectNode writerPropertiesNode = objectMapper.createObjectNode();
		writerPropertiesNode.set("providerId", writerProviderIdNode);
		writerPropertiesNode.set("userId", writerUserIdNode);
		writerPropertiesNode.set("username", writerUsernameNode);

		ObjectNode writerNode = objectMapper.createObjectNode();
		writerNode.set("properties", writerPropertiesNode);

		// properties
		ObjectNode propertiesNode = objectMapper.createObjectNode();
		propertiesNode.set("id", idNode);
		propertiesNode.set("name", nameNode);
		propertiesNode.set("writer", writerNode);

		ObjectNode mappings = objectMapper.createObjectNode();
		mappings.set("properties", propertiesNode);

		return objectMapper.writeValueAsString(mappings);
	}

	private String getSearchWordMappings() throws JsonProcessingException {
		ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

		JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

		ObjectNode propertiesNode = jsonNodeFactory.objectNode();
		propertiesNode.set("id",
				jsonNodeFactory.objectNode()
						.put("type", "string"));
		propertiesNode.set("word",
				jsonNodeFactory.objectNode()
						.put("type", "string")
						.put("index", "not_analyzed")
		);

		ObjectNode writerNode = jsonNodeFactory.objectNode();
		writerNode.set("providerId", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
		writerNode.set("userId", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
		writerNode.set("username", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
		propertiesNode.set("writer", jsonNodeFactory.objectNode().set("properties", writerNode));

		propertiesNode.set("registerDate",
				jsonNodeFactory.objectNode()
						.put("type", "date")
		);

		ObjectNode mappings = jsonNodeFactory.objectNode();
		mappings.set("properties", propertiesNode);

		return objectMapper.writeValueAsString(mappings);
	}
}

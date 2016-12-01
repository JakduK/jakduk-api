package com.jakduk.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.common.util.ObjectMapperUtils;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.ServiceExceptionCode;
import com.jakduk.core.model.elasticsearch.*;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.repository.board.free.BoardFreeCommentRepository;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.jakduk.core.repository.gallery.GalleryRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
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
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
*/

@Slf4j
@Service
public class SearchService {
	
	@Value("${elasticsearch.index.name}")
	private String elasticsearchIndexName;

    @Value("${elasticsearch.enable}")
    private boolean elasticsearchEnable;

	@Value("${core.elasticsearch.index.board}")
	private String elasticsearchIndexBoard;

	@Value("${core.elasticsearch.index.comment}")
	private String elasticsearchIndexComment;

	@Value("${core.elasticsearch.index.gallery}")
	private String elasticsearchIndexGallery;

	@Value("${core.elasticsearch.bulk.actions}")
	private Integer bulkActions;

	@Value("${core.elasticsearch.bulk.size.mb}")
	private Integer bulkMbSize;

	@Value("${core.elasticsearch.bulk.flush.interval.seconds}")
	private Integer bulkFlushIntervalSeconds;

	@Value("${core.elasticsearch.bulk.concurrent.requests}")
	private Integer bulkConcurrentRequests;

	@Autowired
	private JestClient jestClient;

	@Autowired
	private Client client;

	@Autowired
	private BoardFreeRepository boardFreeRepository;

	@Autowired
	private BoardFreeCommentRepository boardFreeCommentRepository;

	@Autowired
	private GalleryRepository galleryRepository;

	/**
	 * 게시물 검색
	 * @param q	검색어
	 * @param from	페이지 시작 위치
	 * @param size	페이지 크기
	 * @return	검색 결과
	 */
	public SearchResult searchDocumentBoard(String q, int from, int size) {
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> query = new HashMap<>();
		Map<String, Object> querySource = new HashMap<>();
		Map<String, Object> queryQuery = new HashMap<>();
		Map<String, Object> queryQueryMultiMatch = new HashMap<>();
		Map<String, Object> queryHighlight = new HashMap<>();
		Map<String, Object> queryHighlightFields = new HashMap<>();
		Map<String, Object> queryScriptFields = new HashMap<>();
		Map<String, Object> queryScriptFieldsContentPreview = new HashMap<>();

		// _source
		querySource.put("exclude", "content");

		// query
		queryQueryMultiMatch.put("fields", new Object[]{"subject", "content"});
		queryQueryMultiMatch.put("query", q);
		queryQuery.put("multi_match", queryQueryMultiMatch);

		// highlight
		queryHighlightFields.put("subject", new HashMap<>());
		queryHighlightFields.put("content", new HashMap<>());
		queryHighlight.put("pre_tags", new Object[]{"<span class=\"color-orange\">"});
		queryHighlight.put("post_tags", new Object[]{"</span>"});
		queryHighlight.put("fields", queryHighlightFields);

		// script_fields
		queryScriptFieldsContentPreview.put("script",
			String.format("_source.content.length() > %d ? _source.content.substring(0, %d) : _source.content",
				CoreConst.SEARCH_CONTENT_MAX_LENGTH,
				CoreConst.SEARCH_CONTENT_MAX_LENGTH
			)
		);

		queryScriptFields.put("content_preview", queryScriptFieldsContentPreview);

		query.put("from", from);
		query.put("size", size);
		query.put("_source", querySource);
		query.put("query", queryQuery);
		query.put("highlight", queryHighlight);
		query.put("script_fields", queryScriptFields);

		try {
			Search search = new Search.Builder(objectMapper.writeValueAsString(query))
					.addIndex(elasticsearchIndexBoard)
					.addType(CoreConst.ES_TYPE_BOARD)
					.build();

			return jestClient.execute(search);

		} catch (IOException e) {
			throw new ServiceException(ServiceExceptionCode.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	public void searchBoardFree(String q, Integer from, Integer size) {
		SearchResponse response = client.prepareSearch()
				.setIndices(elasticsearchIndexBoard)
				.setTypes(CoreConst.ES_TYPE_BOARD)
				.setFetchSource(null, new String[]{"content"})
				.setQuery(QueryBuilders.multiMatchQuery(q, "subject", "content"))
				.addHighlightedField("subject")
				.addHighlightedField("content")
				.setHighlighterPreTags("<span class=\"color-orange\">")
				.setHighlighterPostTags("</span>")
				.addScriptField("content_preview", new Script(
						String.format("_source.content.length() > %d ? _source.content.substring(0, %d) : _source.content",
								CoreConst.SEARCH_CONTENT_MAX_LENGTH,
								CoreConst.SEARCH_CONTENT_MAX_LENGTH))
				)
				.setFrom(from)
				.setSize(size)
				.execute()
				.actionGet();

		SearchHits searchHits = response.getHits();

		Arrays.stream(searchHits.getHits())
				.forEach(searchHit -> {
					Map<String, Object> sourceMap = searchHit.getSource();
					ESBoardFreeSource esBoardFreeSource = ObjectMapperUtils.convertValue(sourceMap, ESBoardFreeSource.class);
					esBoardFreeSource.setScore(searchHit.getScore());
					esBoardFreeSource.setContentPreview(searchHit.getFields().get("content_preview").getValue());
					//esBoardFreeSource.setHighlight(searchHit.getHighlightFields());

					log.debug("phjang=" + searchHit.getHighlightFields().get("subject").getFragments());
				});
	}

	@Async
	public void indexBoardFree(String id, Integer seq, CommonWriter writer, String subject, String content, String category) {

		if (! elasticsearchEnable)
			return;

		ESBoardFree esBoardFree = ESBoardFree.builder()
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
					.setSource(ObjectMapperUtils.writeValueAsString(esBoardFree))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceExceptionCode.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	@Async
	public void deleteBoardFree(String id) {

		if (! elasticsearchEnable)
			return;

		DeleteResponse response = client.prepareDelete()
				.setIndex(elasticsearchIndexName)
				.setType(CoreConst.ES_TYPE_BOARD)
				.setId(id)
				.get();

		if (! response.isFound())
			log.info("board id " + id + " is not found. so can't delete it!");

	}

	public SearchResult searchDocumentComment(String q, int from, int size) {
		Map<String, Object> query = new HashMap<>();
		Map<String, Object> queryQuery = new HashMap<>();
		Map<String, Object> queryQueryMatch = new HashMap<>();
		Map<String, Object> queryHighlight = new HashMap<>();
		Map<String, Object> queryHighlightFields = new HashMap<>();

		queryQueryMatch.put("content", q);
		queryQuery.put("match", queryQueryMatch);

		queryHighlightFields.put("content", new HashMap<>());
		queryHighlight.put("pre_tags", new Object[]{"<span class=\"color-orange\">"});
		queryHighlight.put("post_tags", new Object[]{"</span>"});
		queryHighlight.put("fields", queryHighlightFields);

		query.put("from", from);
		query.put("size", size);
		query.put("query", queryQuery);
		query.put("highlight", queryHighlight);

		Search search = new Search.Builder(new Gson().toJson(query))
			.addIndex(elasticsearchIndexName)
			.addType(CoreConst.ES_TYPE_COMMENT)
			.build();
		
		try {
			return jestClient.execute(search);
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		return null;
	}

	@Async
	public void indexBoardFreeComment(String id, BoardItem boardItem, CommonWriter writer, String content) {

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
					.setIndex(elasticsearchIndexComment)
					.setType(CoreConst.ES_TYPE_COMMENT)
					.setId(id)
					.setSource(ObjectMapperUtils.writeValueAsString(esComment))
					.get();

		} catch (IOException e) {
			throw new ServiceException(ServiceExceptionCode.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	// TODO : 구현 해야 함
	public void createDocumentJakduComment(JakduCommentOnES jakduCommentOnES) {
	}

	public SearchResult searchDocumentJakduComment(String q, int from, int size) {

		String query = "{\n" +
				"\"from\" : " + from + "," +
				"\"size\" : " + size + "," +
				"\"query\": {" +
				"\"match\" : {" +
				"\"content\" : \"" + q + "\"" +
				"}" +
				"}, " +
				"\"highlight\" : {" +
				"\"pre_tags\" : [\"<span class='color-orange'>\"]," +
				"\"post_tags\" : [\"</span>\"]," +
				"\"fields\" : {\"content\" : {}" +
				"}" +
				"}" +
				"}";

//		logger.debug("query=" + query);

		Search search = new Search.Builder(query)
				.addIndex(elasticsearchIndexName)
				.addType(CoreConst.ES_TYPE_JAKDU_COMMENT)
				.build();

		try {
			return jestClient.execute(search);
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		return null;
	}

	public SearchResult searchDocumentGallery(String q, int from, int size) {
		Map<String, Object> query = new HashMap<>();
		Map<String, Object> queryQuery = new HashMap<>();
		Map<String, Object> queryQueryMatch = new HashMap<>();
		Map<String, Object> queryHighlight = new HashMap<>();
		Map<String, Object> queryHighlightFields = new HashMap<>();

		queryQueryMatch.put("name", q);
		queryQuery.put("match", queryQueryMatch);

		queryHighlightFields.put("name", new HashMap<>());
		queryHighlight.put("pre_tags", new Object[]{"<span class=\"color-orange\">"});
		queryHighlight.put("post_tags", new Object[]{"</span>"});
		queryHighlight.put("fields", queryHighlightFields);

		query.put("from", from);
		query.put("size", size);
		query.put("query", queryQuery);
		query.put("highlight", queryHighlight);

		Search search = new Search.Builder(new Gson().toJson(query))
			.addIndex(elasticsearchIndexName)
			.addType(CoreConst.ES_TYPE_GALLERY)
			.build();
		
		try {
			return jestClient.execute(search);
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		return null;
	}

	@Async
	public void indexGallery(String id, CommonWriter writer, String name) {

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
			throw new ServiceException(ServiceExceptionCode.ELASTICSEARCH_INDEX_FAILED, e.getCause());
		}
	}

	@Async
	public void deleteGallery(String id) {

		if (! elasticsearchEnable)
			return;

		DeleteResponse response = client.prepareDelete()
				.setIndex(elasticsearchIndexName)
				.setType(CoreConst.ES_TYPE_GALLERY)
				.setId(id)
				.get();

		if (! response.isFound())
			log.info("gallery id " + id + " is not found. so can't delete it!");
	}

	private Settings.Builder getIndexSettings() {

		//settingsBuilder.put("number_of_shards", 5);
		//settingsBuilder.put("number_of_replicas", 1);

		return Settings.builder()
				.put("index.analysis.analyzer.korean.type", "custom")
				.put("index.analysis.analyzer.korean.tokenizer", "seunjeon_default_tokenizer")
				.put("index.analysis.tokenizer.seunjeon_default_tokenizer.type", "seunjeon_tokenizer");
	}

	public void createIndexBoard() throws IOException {

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

		CreateIndexResponse response = client.admin().indices().prepareCreate(elasticsearchIndexBoard)
				.setSettings(getIndexSettings())
				.addMapping(CoreConst.ES_TYPE_BOARD, objectMapper.writeValueAsString(mappings))
				.get();

		if (response.isAcknowledged()) {
			log.debug("Index " + elasticsearchIndexBoard + " created");
		} else {
			throw new RuntimeException("Index " + elasticsearchIndexBoard + " not created");
		}
	}

	public void createIndexComment() throws JsonProcessingException {

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

		ObjectNode mappings = objectMapper.createObjectNode();
		mappings.set("properties", propertiesNode);

		CreateIndexResponse response = client.admin().indices().prepareCreate(elasticsearchIndexComment)
				.setSettings(getIndexSettings())
				.addMapping(CoreConst.ES_TYPE_COMMENT, objectMapper.writeValueAsString(mappings))
				.get();

		if (response.isAcknowledged()) {
			log.debug("Index " + elasticsearchIndexComment + " created");
		} else {
			throw new RuntimeException("Index " + elasticsearchIndexComment + " not created");
		}
	}

	public void createIndexGallery() throws JsonProcessingException {

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

		CreateIndexResponse response = client.admin().indices().prepareCreate(elasticsearchIndexGallery)
				.setSettings(getIndexSettings())
				.addMapping(CoreConst.ES_TYPE_GALLERY, objectMapper.writeValueAsString(mappings))
				.get();

		if (response.isAcknowledged()) {
			log.debug("Index " + elasticsearchIndexGallery + " created");
		} else {
			throw new RuntimeException("Index " + elasticsearchIndexGallery + " not created");
		}
	}

	public void processBulkInsertBoard() throws InterruptedException {
		BulkProcessor.Listener bulkProcessorListener = new BulkProcessor.Listener() {
			@Override public void beforeBulk(long l, BulkRequest bulkRequest) {
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
				log.error(throwable.getLocalizedMessage());
			}
		};

		BulkProcessor bulkProcessor = BulkProcessor.builder(client, bulkProcessorListener)
				.setBulkActions(bulkActions)
				.setBulkSize(new ByteSizeValue(bulkMbSize, ByteSizeUnit.MB))
				.setFlushInterval(TimeValue.timeValueSeconds(bulkFlushIntervalSeconds))
				.setConcurrentRequests(bulkConcurrentRequests)
				.build();

		Boolean hasPost = true;
		ObjectId lastPostId = null;

		do {
			List<ESBoardFree> posts = boardFreeRepository.findPostsGreaterThanId(lastPostId, CoreConst.ES_BULK_LIMIT);

			if (posts.isEmpty()) {
				hasPost = false;
			} else {
				ESBoardFree lastPost = posts.get(posts.size() - 1);
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
		BulkProcessor.Listener bulkProcessorListener = new BulkProcessor.Listener() {
			@Override public void beforeBulk(long l, BulkRequest bulkRequest) {
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
				log.error(throwable.getLocalizedMessage());
			}
		};

		BulkProcessor bulkProcessor = BulkProcessor.builder(client, bulkProcessorListener)
				.setBulkActions(bulkActions)
				.setBulkSize(new ByteSizeValue(bulkMbSize, ByteSizeUnit.MB))
				.setFlushInterval(TimeValue.timeValueSeconds(bulkFlushIntervalSeconds))
				.setConcurrentRequests(bulkConcurrentRequests)
				.build();

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
				IndexRequestBuilder index = client.prepareIndex(
						elasticsearchIndexComment,
						CoreConst.ES_TYPE_COMMENT,
						comment.getId()
				);

				try {

					index.setSource(ObjectMapperUtils.writeValueAsString(comment));
					bulkProcessor.add(index.request());

				} catch (JsonProcessingException e) {
					log.error(e.getLocalizedMessage());
				}

			});

		} while (hasComment);

		bulkProcessor.awaitClose(CoreConst.ES_AWAIT_CLOSE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
	}

	public void processBulkInsertGallery() throws InterruptedException {
		BulkProcessor.Listener bulkProcessorListener = new BulkProcessor.Listener() {
			@Override public void beforeBulk(long l, BulkRequest bulkRequest) {
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
			}

			@Override public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
				log.error(throwable.getLocalizedMessage());
			}
		};

		BulkProcessor bulkProcessor = BulkProcessor.builder(client, bulkProcessorListener)
				.setBulkActions(bulkActions)
				.setBulkSize(new ByteSizeValue(bulkMbSize, ByteSizeUnit.MB))
				.setFlushInterval(TimeValue.timeValueSeconds(bulkFlushIntervalSeconds))
				.setConcurrentRequests(bulkConcurrentRequests)
				.build();

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

		DeleteIndexResponse response = client.admin().indices()
				.delete(new DeleteIndexRequest(elasticsearchIndexBoard))
				.actionGet();

		if (response.isAcknowledged()) {
			log.debug("Index " + elasticsearchIndexBoard + " deleted");
		} else {
			throw new RuntimeException("Index " + elasticsearchIndexBoard + " not deleted");
		}
	}

	public void deleteIndexComment() {

		DeleteIndexResponse response = client.admin().indices()
				.delete(new DeleteIndexRequest(elasticsearchIndexComment))
				.actionGet();

		if (response.isAcknowledged()) {
			log.debug("Index " + elasticsearchIndexComment + " deleted");
		} else {
			throw new RuntimeException("Index " + elasticsearchIndexComment + " not deleted");
		}
	}

	public void deleteIndexGallery() {

		DeleteIndexResponse response = client.admin().indices()
				.delete(new DeleteIndexRequest(elasticsearchIndexGallery))
				.actionGet();

		if (response.isAcknowledged()) {
			log.debug("Index " + elasticsearchIndexGallery + " deleted");
		} else {
			throw new RuntimeException("Index " + elasticsearchIndexGallery + " not deleted");
		}
	}
}

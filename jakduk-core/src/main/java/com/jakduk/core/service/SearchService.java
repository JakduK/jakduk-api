package com.jakduk.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.dao.BoardDAO;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.model.elasticsearch.BoardFreeOnES;
import com.jakduk.core.model.elasticsearch.CommentOnES;
import com.jakduk.core.model.elasticsearch.GalleryOnES;
import com.jakduk.core.model.elasticsearch.JakduCommentOnES;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
*/

@Slf4j
@Service
public class SearchService {
	
	@Value("${elasticsearch.index.name}")
	private String elasticsearchIndexName;

	@Value("${elasticsearch.host.name}")
	private String elasticsearchHostName;
	
	@Autowired
	private JestClient jestClient;

	@Autowired
	private Client client;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BoardDAO boardDAO;

	@Autowired
	private JakdukDAO jakdukDAO;

	public Map<String, Object> getSearch(Locale locale, String q, String w, int from, int size) {
		Map<String, Object> data = new HashMap<>();
		data.put("q", q);
		data.put("w", w);
		data.put("from", from);
		data.put("size", size);
		try {
			data.put("dateTimeFormat", new ObjectMapper().writeValueAsString(commonService.getDateTimeFormat(locale)));
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}

		return data;
	}
	
	public Map<String, Object> getDataSearch(String q, String w, int from, int size) {
		Map<String, Object> data = new HashMap<>();

		if (log.isDebugEnabled()) {
			log.debug("q=" + q + ", w=" + w + ", from=" + from + ", size=" + size);
		}
		
		if (size <= 0) size = 10;
		
		if (w != null && !w.isEmpty()) {
			if (w.contains("PO")) {
				SearchResult result = this.searchDocumentBoard(q, from, size);
				if (result.isSucceeded()) {
					data.put("posts", result.getJsonString());
				}
			}

			if (w.contains("CO")) {
				List<ObjectId> ids = new ArrayList<>();
				SearchResult result = this.searchDocumentComment(q, from, size);

				if (result.isSucceeded()) {
					List<SearchResult.Hit<CommentOnES, Void>> hits = result.getHits(CommentOnES.class);
					hits.forEach(hit -> {
						String id = hit.source.getBoardItem().getId();
						ids.add(new ObjectId(id));
					});
					data.put("comments", result.getJsonString());
					data.put("postsHavingComments", boardDAO.getBoardFreeOnSearchComment(ids));
				}
			}

			if (w.contains("GA")) {
				int tempSize = size;
				
				if (size < 10) {
					tempSize = 4;
				}
				
				SearchResult result = this.searchDocumentGallery(q, from, tempSize);
				if (result.isSucceeded()) {
					data.put("galleries", result.getJsonString());
				}
			}
		}

		return data;
	}
	
	public SearchResult searchDocumentBoard(String q, int from, int size) {
		Map<String, Object> query = new HashMap<>();
		Map<String, Object> querySource = new HashMap<>();
		Map<String, Object> queryQuery = new HashMap<>();
		Map<String, Object> queryQueryMultiMatch = new HashMap<>();
		Map<String, Object> queryHighlight = new HashMap<>();
		Map<String, Object> queryHighlightFields = new HashMap<>();
		Map<String, Object> queryScriptFields = new HashMap<>();
		Map<String, Object> queryScriptFieldsContentPreview = new HashMap<>();

		querySource.put("exclude", "content");

		queryQueryMultiMatch.put("fields", new Object[]{"subject", "content"});
		queryQueryMultiMatch.put("query", q);
		queryQuery.put("multi_match", queryQueryMultiMatch);

		queryHighlightFields.put("subject", new HashMap<>());
		queryHighlightFields.put("content", new HashMap<>());
		queryHighlight.put("pre_tags", new Object[]{"<span class=\"color-orange\">"});
		queryHighlight.put("post_tags", new Object[]{"</span>"});
		queryHighlight.put("fields", queryHighlightFields);

		queryScriptFieldsContentPreview.put("script",
			String.format("_source.content.length() > %d ? _source.content.substring(0, %d) : _source.content",
				CommonConst.SEARCH_CONTENT_MAX_LENGTH,
				CommonConst.SEARCH_CONTENT_MAX_LENGTH
			)
		);
		queryScriptFields.put("content_preview", queryScriptFieldsContentPreview);

		query.put("from", from);
		query.put("size", size);
		query.put("_source", querySource);
		query.put("query", queryQuery);
		query.put("highlight", queryHighlight);
		query.put("script_fields", queryScriptFields);

		Search search = new Search.Builder(new Gson().toJson(query))
				.addIndex(elasticsearchIndexName)
				.addType(CommonConst.ELASTICSEARCH_TYPE_BOARD)
				.build();
		
		try {
			return jestClient.execute(search);
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		return null;
	}
	
	public void createDocumentBoard(BoardFreeOnES boardFreeOnEs) {
		Index index = new Index.Builder(boardFreeOnEs).index(elasticsearchIndexName).type(CommonConst.ELASTICSEARCH_TYPE_BOARD).build();
		
		try {
			JestResult jestResult = jestClient.execute(index);
			if (!jestResult.isSucceeded()) {
				log.error(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
	}
	
	public void deleteDocumentBoard(String id) {
        try {
			JestResult jestResult = jestClient.execute(new Delete.Builder(id)
			        .index(elasticsearchIndexName)
			        .type(CommonConst.ELASTICSEARCH_TYPE_BOARD)
			        .build());
			
			if (log.isDebugEnabled()) {
				if (jestResult.getValue("found") != null && jestResult.getValue("found").toString().equals("false")) {
					log.debug("board id " + id + " is not found. so can't delete it!");
				}
			}
			
			if (!jestResult.isSucceeded()) {
				log.error(jestResult.getErrorMessage());
			}
			
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
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
			.addType(CommonConst.ELASTICSEARCH_TYPE_COMMENT)
			.build();
		
		try {
			return jestClient.execute(search);
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		return null;
	}

	public void createDocumentComment(CommentOnES commentOnES) {
		Index index = new Index.Builder(commentOnES).index(elasticsearchIndexName).type(CommonConst.ELASTICSEARCH_TYPE_COMMENT).build();

		try {
			JestResult jestResult = jestClient.execute(index);
			if (!jestResult.isSucceeded()) {
				log.error(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
	}

	public void createDocumentJakduComment(JakduCommentOnES jakduCommentOnES) {
		Index index = new Index.Builder(jakduCommentOnES).index(elasticsearchIndexName).type(CommonConst.ELASTICSEARCH_TYPE_COMMENT).build();

		try {
			JestResult jestResult = jestClient.execute(index);
			if (!jestResult.isSucceeded()) {
				log.error(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
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
				.addType(CommonConst.ELASTICSEARCH_TYPE_JAKDU_COMMENT)
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
			.addType(CommonConst.ELASTICSEARCH_TYPE_GALLERY)
			.build();
		
		try {
			return jestClient.execute(search);
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		return null;
	}
	
	public void createDocumentGallery(GalleryOnES galleryOnES) {
		Index index = new Index.Builder(galleryOnES).index(elasticsearchIndexName).type(CommonConst.ELASTICSEARCH_TYPE_GALLERY).build();
		
		try {
			JestResult jestResult = jestClient.execute(index);
			if (!jestResult.isSucceeded()) {
				log.error(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
	}
	
	public void deleteDocumentGallery(String id) {
        try {
			JestResult jestResult = jestClient.execute(new Delete.Builder(id)
			        .index(elasticsearchIndexName)
			        .type(CommonConst.ELASTICSEARCH_TYPE_GALLERY)
			        .build());
			
			if (log.isDebugEnabled()) {
				if (jestResult.getValue("found") != null && jestResult.getValue("found").toString().equals("false")) {
					log.debug("gallery id " + id + " is not found. so can't delete it!");
				}
			}
			
			if (!jestResult.isSucceeded()) {
				log.error(jestResult.getErrorMessage());
			}
			
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
	}

	/**
	 * 엘라스틱서치 인덱스 초기화.
	 * @return
	 */
	public HashMap<String, Object> initSearchIndex() {

		HashMap<String, Object> result = new HashMap<>();

		Settings.Builder builder = Settings.builder();
		//settingsBuilder.put("number_of_shards", 5);
		//settingsBuilder.put("number_of_replicas", 1);
		builder.put("index.analysis.analyzer.korean.type", "custom");
		builder.put("index.analysis.analyzer.korean.tokenizer", "seunjeon_default_tokenizer");
		builder.put("index.analysis.tokenizer.seunjeon_default_tokenizer.type", "seunjeon_tokenizer");

		try {
			JestResult jestResult = jestClient.execute(
					new CreateIndex.Builder(elasticsearchIndexName)
							.settings(builder.build().getAsMap())
							.build()
			);

			if (! jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			} else {
				result.put("result", Boolean.TRUE);
			}
		} catch (IOException e) {
			result.put("result", Boolean.FALSE);
			log.error(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 엘라스틱서치 매핑 초기화.
	 * @return
	 */
	public HashMap<String, Object> initSearchType() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> source = new HashMap<>();

		// 게시물 매핑 초기화.
		Map<String, Object> properties = new HashMap<>();
		Map<String, Object> subject = new HashMap<>();
		Map<String, Object> content = new HashMap<>();

		subject.put("type", "string");
		subject.put("analyzer", "korean");
		content.put("type", "string");
		content.put("analyzer", "korean");
		properties.put("subject", subject);
		properties.put("content", content);
		source.put("properties", properties);

		PutMapping putMapping1 = new PutMapping.Builder(
				elasticsearchIndexName,
				CommonConst.ELASTICSEARCH_TYPE_BOARD,
				objectMapper.writeValueAsString(source)
		).build();

		// 댓글 매핑 초기화.
		properties = new HashMap<>();
		source = new HashMap<>();
		content = new HashMap<>();

		content.put("type", "string");
		content.put("analyzer", "korean");
		properties.put("content", content);
		source.put("properties", properties);

		PutMapping putMapping2 = new PutMapping.Builder(
				elasticsearchIndexName,
				CommonConst.ELASTICSEARCH_TYPE_COMMENT,
				objectMapper.writeValueAsString(source)
		).build();

		// 사진첩 매핑 초기화.
		properties = new HashMap<>();
		source = new HashMap<>();
		content = new HashMap<>();

		content.put("type", "string");
		content.put("analyzer", "korean");
		properties.put("name", content);
		source.put("properties", properties);

		PutMapping putMapping3 = new PutMapping.Builder(
				elasticsearchIndexName,
				CommonConst.ELASTICSEARCH_TYPE_GALLERY,
				objectMapper.writeValueAsString(source)
		).build();

		HashMap<String, Object> result = new HashMap<>();

		try {
			JestResult jestResult1 = jestClient.execute(putMapping1);
			JestResult jestResult2 = jestClient.execute(putMapping2);
			JestResult jestResult3 = jestClient.execute(putMapping3);

			if (! jestResult1.isSucceeded())
				log.debug(jestResult1.getErrorMessage());

			if (! jestResult2.isSucceeded())
				log.debug(jestResult2.getErrorMessage());

			if (! jestResult3.isSucceeded())
				log.debug(jestResult3.getErrorMessage());

		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * bulk document 생성.
	 * @return
	 */
	public HashMap<String, Object> initSearchDocuments() {

		HashMap<String, Object> result = new HashMap<>();

		// 게시물을 엘라스틱 서치에 모두 넣기.
		List<BoardFreeOnES> posts = jakdukDAO.getBoardFreeOnES(null);
		BoardFreeOnES lastPost = posts.size() > 0 ? posts.get(posts.size() - 1) : null;

		while (posts.size() > 0) {
			List<Index> idxList = new ArrayList<>();

			for (BoardFreeOnES post : posts) {
				idxList.add(new Index.Builder(post).build());
			}

			Bulk bulk = new Bulk.Builder()
					.defaultIndex(elasticsearchIndexName)
					.defaultType(CommonConst.ELASTICSEARCH_TYPE_BOARD)
					.addAction(idxList)
					.build();

			try {
				JestResult jestResult = jestClient.execute(bulk);
				if (! jestResult.isSucceeded()) {
					log.debug(jestResult.getErrorMessage());
				}
			} catch (IOException e) {
				log.warn(e.getMessage(), e);
			}

			if (Objects.nonNull(lastPost)) {
				posts = jakdukDAO.getBoardFreeOnES(new ObjectId(lastPost.getId()));
				if (posts.size() > 0) {
					lastPost = posts.get(posts.size() - 1);
				}
			}
		}

		// 게시물을 엘라스틱 서치에 모두 넣기.
		List<CommentOnES> comments = jakdukDAO.getCommentOnES(null);
		CommentOnES lastComment = comments.size() > 0 ? comments.get(comments.size() - 1) : null;

		while (comments.size() > 0) {
			List<Index> idxList = new ArrayList<>();

			for (CommentOnES comment : comments) {
				idxList.add(new Index.Builder(comment).build());
			}

			Bulk bulk = new Bulk.Builder()
					.defaultIndex(elasticsearchIndexName)
					.defaultType(CommonConst.ELASTICSEARCH_TYPE_COMMENT)
					.addAction(idxList)
					.build();

			try {
				JestResult jestResult = jestClient.execute(bulk);
				if (! jestResult.isSucceeded()) {
					log.debug(jestResult.getErrorMessage());
				}
			} catch (IOException e) {
				log.warn(e.getMessage(), e);
			}

			if (Objects.nonNull(lastComment)) {
				comments = jakdukDAO.getCommentOnES(new ObjectId(lastComment.getId()));
				if (comments.size() > 0) {
					lastComment = comments.get(comments.size() - 1);
				}
			}
		}

		// 사진첩을 엘라스틱 서치에 모두 넣기.
		List<GalleryOnES> galleries = jakdukDAO.getGalleryOnES(null);
		GalleryOnES lastGallery = galleries.size() > 0 ? galleries.get(galleries.size() - 1) : null;

		while (galleries.size() > 0) {
			List<Index> idxList = new ArrayList<>();

			for (GalleryOnES gallery : galleries) {
				idxList.add(new Index.Builder(gallery).build());
			}

			Bulk bulk = new Bulk.Builder()
					.defaultIndex(elasticsearchIndexName)
					.defaultType(CommonConst.ELASTICSEARCH_TYPE_GALLERY)
					.addAction(idxList)
					.build();

			try {
				JestResult jestResult = jestClient.execute(bulk);
				if (! jestResult.isSucceeded()) {
					log.debug(jestResult.getErrorMessage());
				}
			} catch (IOException e) {
				log.warn(e.getMessage(), e);
			}

			if (Objects.nonNull(lastGallery)) {
				galleries = jakdukDAO.getGalleryOnES(new ObjectId(lastGallery.getId()));
				if (galleries.size() > 0) {
					lastGallery = galleries.get(galleries.size() - 1);
				}
			}
		}

		return result;
	}

	public void deleteIndex() throws UnknownHostException {

		DeleteIndexResponse response = client.admin().indices()
				.delete(new DeleteIndexRequest(elasticsearchIndexName))
				.actionGet();

		log.debug(response.toString());

	}
}

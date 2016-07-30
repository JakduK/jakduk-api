package com.jakduk.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardDAO;
import com.jakduk.model.elasticsearch.BoardFreeOnES;
import com.jakduk.model.elasticsearch.CommentOnES;
import com.jakduk.model.elasticsearch.GalleryOnES;
import com.jakduk.model.elasticsearch.JakduCommentOnES;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
*/

@Service
@Slf4j
public class SearchService {
	
	@Value("${elasticsearch.index.name}")
	private String elasticsearchIndexName;
	
	@Autowired
	private JestClient jestClient;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BoardDAO boardDAO;

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
				data.put("posts", this.searchDocumentBoard(q, from, size));
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
				}

				data.put("comments", result.getJsonString());
				data.put("postsHavingComments", boardDAO.getBoardFreeOnSearchComment(ids));
			}

			if (w.contains("GA")) {
				int tempSize = size;
				
				if (size < 10) {
					tempSize = 4;
				}
				
				SearchResult result = this.searchDocumentGallery(q, from, tempSize);
				data.put("galleries", result.getJsonString());
			}
		}

		return data;
	}
	
	public String searchDocumentBoard(String q, int from, int size) {
		
		String query = "{\n" +
				//"\"fields\" : [\"seq\", \"writer.type\", \"writer.userId\", \"writer.username\", \"subject\", \"contentPreview\"]," +
				"\"from\" : " + from + "," + 
				"\"size\" : " + size + "," + 
				"\"_source\" : { \"exclude\" : \"content\"}, " +
				"\"query\": {" +
				"\"multi_match\" : {" +
				"\"fields\" : [\"subject\", \"content\"]," +
				"\"query\" : \"" + q + "\"" + 
				"}" +
				"}, " +
				"\"highlight\" : {" +
				"\"pre_tags\" : [\"<span class='color-orange'>\"]," +
				"\"post_tags\" : [\"</span>\"]," +
				"\"fields\" : { \"subject\" : {}, \"content\" : {}" +
				"}" + 
				"}," +
				"\"script_fields\" : {" +
				"\"content_preview\" : {" +
				"\"script\" : \"_source.content.length() > " + CommonConst.SEARCH_CONTENT_MAX_LENGTH + 
				"? _source.content.substring(0," + CommonConst.SEARCH_CONTENT_MAX_LENGTH + ") : _source.content\"" +
				"}" +
				"}" +				
				"}";

//		logger.debug("query=" + query);

		Search search = new Search.Builder(query)
				.addIndex(elasticsearchIndexName)
				.addType(CommonConst.ELASTICSEARCH_TYPE_BOARD)
				.build();
		
		try {
			return jestClient.execute(search).getJsonObject().toString();
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		return "";
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
		String query = "{\n" +
				"\"from\" : " + from + "," + 
				"\"size\" : " + size + "," + 
				"\"query\": {" +
				"\"match\" : {" +
				"\"name\" : \"" + q + "\"" + 
				"}" +
				"}, " +
				"\"highlight\" : {" +
				"\"pre_tags\" : [\"<span class='color-orange'>\"]," +
				"\"post_tags\" : [\"</span>\"]," +
				"\"fields\" : {\"name\" : {}" +
				"}" + 
				"}" +
				"}";

		Search search = new Search.Builder(query)
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
}

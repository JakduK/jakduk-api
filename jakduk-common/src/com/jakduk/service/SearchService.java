package com.jakduk.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardDAO;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.elasticsearch.BoardFreeOnES;
import com.jakduk.model.elasticsearch.CommentOnES;
import com.jakduk.model.elasticsearch.GalleryOnES;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@Service
public class SearchService {
	
	@Value("${elasticsearch.index.name}")
	private String elasticsearchIndexName;
	
	@Autowired
	private JestClient jestClient;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BoardDAO boardDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());

	public void getSearch(Model model, Locale locale, String q, String w, int from, int size) {
		model.addAttribute("q", q);
		model.addAttribute("w", w);
		model.addAttribute("from", from);
		model.addAttribute("size", size);
		try {
			model.addAttribute("dateTimeFormat", new ObjectMapper().writeValueAsString(commonService.getDateTimeFormat(locale)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getDataSearch(Model model, String q, String w, int from, int size) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("q=" + q + ", w=" + w + ", from=" + from + ", size=" + size);
		}
		
		if (size <= 0) size = 10;
		
		if (w != null && !w.isEmpty()) {
			if (w.contains("PO")) {
				model.addAttribute("posts", this.searchDocumentBoard(q, from, size));				
			}
			if (w.contains("CO")) {
				List<ObjectId> ids = new ArrayList<ObjectId>();
				
				SearchResult result = this.searchDocumentComment(q, from, size);
				
				List<SearchResult.Hit<CommentOnES, Void>> hits = result.getHits(CommentOnES.class);
				
				Iterator<SearchResult.Hit<CommentOnES, Void>> hitsItr = hits.iterator();
				
				while (hitsItr.hasNext()) {
					SearchResult.Hit<CommentOnES, Void> itr = hitsItr.next();
					
					String id = itr.source.getBoardItem().getId();
					
					ids.add(new ObjectId(id));
				}
				
				model.addAttribute("comments", result.getJsonString());
				model.addAttribute("postsHavingComments", boardDAO.getBoardFreeOnSearchComment(ids));
			}
			if (w.contains("GA")) {
				int tempSize = size;
				
				if (size < 10) {
					tempSize = 4;
				}
				
				SearchResult result = this.searchDocumentGallery(q, from, tempSize);
				model.addAttribute("galleries", result.getJsonString());
			}
		}
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
			SearchResult result = jestClient.execute(search);
			
			return result.getJsonObject().toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public void createDocumentBoard(BoardFreeOnES boardFreeOnEs) {
		Index index = new Index.Builder(boardFreeOnEs).index(elasticsearchIndexName).type(CommonConst.ELASTICSEARCH_TYPE_BOARD).build();
		
		try {
			JestResult jestResult = jestClient.execute(index);
			
			if (!jestResult.isSucceeded()) {
				logger.error(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteDocumentBoard(String id) {
        try {
			JestResult jestResult = jestClient.execute(new Delete.Builder(id)
			        .index(elasticsearchIndexName)
			        .type(CommonConst.ELASTICSEARCH_TYPE_BOARD)
			        .build());
			
			if (logger.isDebugEnabled()) {
				if (jestResult.getValue("found") != null && jestResult.getValue("found").toString().equals("false")) {
					logger.debug("board id " + id + " is not found. so can't delete it!");
				}
			}
			
			if (!jestResult.isSucceeded()) {
				logger.error(jestResult.getErrorMessage());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			SearchResult result = jestClient.execute(search);
			
			return result;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void createDocumentComment(CommentOnES commentOnES) {
		Index index = new Index.Builder(commentOnES).index(elasticsearchIndexName).type(CommonConst.ELASTICSEARCH_TYPE_COMMENT).build();
		
		try {
			JestResult jestResult = jestClient.execute(index);
			
			if (!jestResult.isSucceeded()) {
				logger.error(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			SearchResult result = jestClient.execute(search);
			
			return result;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void createDocumentGallery(GalleryOnES galleryOnES) {
		Index index = new Index.Builder(galleryOnES).index(elasticsearchIndexName).type(CommonConst.ELASTICSEARCH_TYPE_GALLERY).build();
		
		try {
			JestResult jestResult = jestClient.execute(index);
			
			if (!jestResult.isSucceeded()) {
				logger.error(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteDocumentGallery(String id) {
        try {
			JestResult jestResult = jestClient.execute(new Delete.Builder(id)
			        .index(elasticsearchIndexName)
			        .type(CommonConst.ELASTICSEARCH_TYPE_GALLERY)
			        .build());
			
			if (logger.isDebugEnabled()) {
				if (jestResult.getValue("found") != null && jestResult.getValue("found").toString().equals("false")) {
					logger.debug("gallery id " + id + " is not found. so can't delete it!");
				}
			}
			
			if (!jestResult.isSucceeded()) {
				logger.error(jestResult.getErrorMessage());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}

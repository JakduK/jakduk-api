package com.jakduk.service;

import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.model.elasticsearch.BoardFreeOnES;

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
	
	private Logger logger = Logger.getLogger(this.getClass());

	public void getSearchBoard(Model model, Locale locale, String q, int from, int size) {
		model.addAttribute("q", q);
		model.addAttribute("from", from);
		model.addAttribute("size", size);
		model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
	}
	
	public void searchDataOfBoard(Model model, String q, int from, int size) {
		
		if (size <= 0) size = 10;
		
		model.addAttribute("results", this.searchDocumentBoard(q, from, size));
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
				.addType("board")
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
		Index index = new Index.Builder(boardFreeOnEs).index(elasticsearchIndexName).type("board").build();
		
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
			        .type("board")
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
}

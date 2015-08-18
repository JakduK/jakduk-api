package com.jakduk.service;

import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.model.elasticsearch.BoardFreeOnES;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
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

	public void getSearchBoard(Model model, Locale locale, String q) {
		model.addAttribute("q", q);
		model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
	}
	
	/*
	public void searchDataOfBoard(Model model, String q) {
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(q, "subject", "content"));
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex(elasticsearchIndexName)
				.build();
		
		try {
			SearchResult result = jestClient.execute(search);
//			System.out.println("result=" + result.getJsonString());
			
			model.addAttribute("results", result.getJsonObject().toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	public void searchDataOfBoard(Model model, String q) {
		
		String query = "{\n" +
				//"\"fields\" : [\"seq\", \"writer.type\", \"writer.userId\", \"writer.username\", \"subject\", \"contentPreview\"]," +
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
				"} }" +
				"}";

//		logger.debug("query=" + query);

	Search search = new Search.Builder(query)
	                .addIndex(elasticsearchIndexName)
	                .addType("board")
	                .build();
		
		try {
			SearchResult result = jestClient.execute(search);
			
			model.addAttribute("results", result.getJsonObject().toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}

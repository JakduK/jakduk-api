package com.jakduk.service;

import java.io.IOException;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
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
	
	@Autowired
	private JestClient jestClient;

	public JsonObject searchBoard(String q) {
		
		JsonObject jsonObj = new JsonObject();
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("content", q));
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex("articles")
				.build();
		
		try {
			SearchResult result = jestClient.execute(search);
//			System.out.println("result=" + result.getJsonString());
			
			jsonObj = result.getJsonObject();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
	}
}

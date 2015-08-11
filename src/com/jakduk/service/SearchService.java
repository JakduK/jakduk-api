package com.jakduk.service;

import java.io.IOException;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

	public void getSearchBoard(Model model, String q) {
		model.addAttribute("q", q);
	}
	
	public void searchDataOfBoard(Model model, String q) {
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(q, "subject", "content"));
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex("jakduk")
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
}

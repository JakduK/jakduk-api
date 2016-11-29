package com.jakduk.core.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.model.elasticsearch.ESBoardFree;
import com.jakduk.core.model.elasticsearch.ESComment;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.service.SearchService;
import com.jakduk.core.util.AbstractSpringTest;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;
import org.bson.types.ObjectId;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 2.
* @desc     :
*/

public class JestTest extends AbstractSpringTest {
	
	@Autowired
	private JestClient jestClient;
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Autowired
	private SearchService sut;

	@Autowired
	private Client client;
	
	@Before
	public void before() {
		/*
		String[] result = elasticsearchHostName.split(",");
		
    	JestClientFactory factory = new JestClientFactory();
    	factory.setHttpClientConfig(new HttpClientConfig
    			.Builder(Arrays.asList(result))
    			.multiThreaded(true)
    			.build());
    	
    	client = factory.getObject();
    	*/
		
	}
	
	@Test
	public void createIndex() throws IOException {
		//client.execute(new CreateIndex.Builder("articles").build());
		
		/*
		String settings = "\"settings\" : {\n" +
                "        \"number_of_shards\" : 3,\n" +
                "        \"number_of_replicas\" : 1\n" +
                "    }\n";
		 */
		
		System.out.println("jestClient=" + jestClient);
		
		Settings.Builder settingsBuilder = Settings.settingsBuilder();
		settingsBuilder.put("number_of_shards", 5);
		settingsBuilder.put("number_of_replicas", 1);
		settingsBuilder.put("index.analysis.analyzer.korean.type", "custom");
		settingsBuilder.put("index.analysis.analyzer.korean.tokenizer", "seunjeon_default_tokenizer");
		settingsBuilder.put("index.analysis.tokenizer.seunjeon_default_tokenizer.type", "seunjeon_tokenizer");

		JestResult result = jestClient.execute(new CreateIndex.Builder("articles").settings(settingsBuilder.build().getAsMap()).build());
		System.out.println("result=" + result.getErrorMessage());
	}
	
	@Test
	public void mapping01() {
		/*
		RootObjectMapper.Builder rootObjectMapperBuilder = new RootObjectMapper.Builder("tweet")
				.add(new StringFieldMapper.Builder("message").store(true));
		
		DocumentMapper documentMapper = new DocumentMapper.Builder("articles", settingsBuilder.build(), rootObjectMapperBuilder).build(null);
		String expectedMappingSource = documentMapper.mappingSource().toString();
		System.out.println("expectedMappingSource=" + expectedMappingSource);
		PutMapping putMapping = new PutMapping.Builder("articles", "tweet", expectedMappingSource).build();
		jestClient.execute(putMapping);
		*/
		
        PutMapping putMapping = new PutMapping.Builder(
                "articles",
                "tweet",
                "{ \"properties\" : { \"subject\" : {\"type\" : \"string\", \"analyzer\" : \"korean\"}"
                + ", \"content\" : {\"type\" : \"string\", \"analyzer\" : \"korean\"} }"
                + "}"
        ).build();

		try {
			JestResult result = jestClient.execute(putMapping);
			System.out.println("result=" + result.getErrorMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapping02() {
        PutMapping putMapping = new PutMapping.Builder(
                "articles",
                "comment",
                "{ \"properties\" : { " +
                        "\"content\" : {\"type\" : \"string\", \"analyzer\" : \"korean\"} " +
                        	"}" +
                          "}"
        ).build();

		try {
			JestResult result = jestClient.execute(putMapping);
			System.out.println("result=" + result.getErrorMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void createDocument() {
		Article source = new Article();
		
		CommonWriter writer = new CommonWriter();
		writer.setUsername("John Ronald Reuel Tolkien");
		source.setWriter(writer);
		source.setSubject("The Lord of the Rings is an epic high fantasy novel");
		
		Index index = new Index.Builder(source).index("articles").type("tweet").build();
		try {
			jestClient.execute(index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void search01() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("content", "건강"));
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex("articles")
				.build();
		System.out.println("search=" + search.getRestMethodName());
		System.out.println("search=" + search.getPathToResult());
		
		try {
			SearchResult result = jestClient.execute(search);
//			System.out.println("result=" + result.getJsonString());

			JsonObject jsonObj = result.getJsonObject();
			
			System.out.println("jsonObj=" + jsonObj);
						
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void search02() {
		
		String query = "{\n" +
//				"\"fields\" : [\"seq\", \"writer.type\", \"writer.userId\", \"writer.username\", \"subject\", \"contentPreview\"]," +
//				"\"_source\" : { \"include\" : \"contentPreview\"}, " +
				"\"query\": {\n" +
				"\"multi_match\" : {" +
				"\"fields\" : [\"subject\", \"content\"]," +
				"\"query\" : \"oiuiou\"" +
				"}\n" +
				"}, " +
				"\"highlight\" : {" +
				"\"pre_tags\" : [\"<span class='color-sea'>\"]," +
				"\"post_tags\" : [\"</span>\"]," +
				"\"fields\" : { \"subject\" : {}, \"content\" : {} } }" +
				"}";
		
		//System.out.println("query=" + query);

	Search search = new Search.Builder(query)
	                // multiple index or types can be added.
	                .addIndex("jakduk_test")
	                .build();

	try {
		SearchResult result = jestClient.execute(search);
		
		System.out.println("jsonObj=" + result.getJsonString());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	@Test
	public void search03() {

		String query = "{\n" +
				//				"\"fields\" : [\"seq\", \"writer.type\", \"writer.userId\", \"writer.username\", \"subject\", \"contentPreview\"]," +
				"\"_source\" : { \"exclude\" : \"content\"}, " +
				"\"from\" : 2," +
				"\"size\" : 2," +
				"\"query\": {\n" +
				"\"multi_match\" : {" +
				"\"fields\" : [\"subject\", \"content\"]," +
				"\"query\" : \"축구\"" + 
				"}\n" +
				"}, " +
				"\"highlight\" : {" +
				"\"pre_tags\" : [\"<span class='color-sea'>\"]," +
				"\"post_tags\" : [\"</span>\"]," +
				"\"fields\" : { \"subject\" : {}, \"content\" : {} } " +
				"}," +
				"\"script_fields\" : {" +
				"\"field1_substring\" : {" +
				"\"script\" : \"_source.content.length() > 10 ? _source.content.substring(0,10) : _source.content\"" +
				"}" +
				"}" +
				"}";

		//System.out.println("query=" + query);

		Search search = new Search.Builder(query)
				// multiple index or types can be added.
				.addIndex("articles")
				.build();

		try {
			SearchResult result = jestClient.execute(search);

			System.out.println("jsonObj=" + result.getJsonString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void search04() {
		System.out.println("result=" + sut.searchDocumentBoard("조니", 0, 10).getJsonString());
		//System.out.println(sut.searchDocumentComment("댓글", 0, 10));
	}
	
	@Test
	public void search05() {
		String query = "{\n" +
				"\"query\": {" +
				"\"match\" : {" +
				"\"content\" : \"테스트\"" +
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
				.addIndex("jakduk_test")
				.addType(CoreConst.ES_TYPE_COMMENT)
				.build();
		
		try {
			SearchResult result = jestClient.execute(search);
			
			JsonObject jsonObject = result.getJsonObject();
			
			List<SearchResult.Hit<ESComment, Void>> hits = result.getHits(ESComment.class);
			
			Iterator<SearchResult.Hit<ESComment, Void>> hitsItr = hits.iterator();
			
			while (hitsItr.hasNext()) {
				SearchResult.Hit<ESComment, Void> itr = hitsItr.next();
				System.out.println(itr.source.getBoardItem());
			}
			
			/*
			for (SearchResult.Hit<ESComment, Void> hit : hits) {
				System.out.println(hit.source.getBoardItem());
			}
			*/
			
			System.out.println("result=" + result.getJsonString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void delete01() {
        try {
			JestResult result = jestClient.execute(new Delete.Builder("54a8a3d9e4b05110382b4409")
			        .index("articles")
			        .type("tweet")
			        .build());
			
			System.out.println("result=" + result.getJsonString());
			System.out.println("result=" + result.getValue("found"));
			
			if (result.getValue("found").toString().equals("false")) {
				System.out.println("not found document.");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void bulk01() {
		/*
		Article source = new Article();
		Article source2 = new Article();
		source.setAuthor("John Ronald Reuel Tolkien");
		source.setContent("The Lord of the Rings is an epic high fantasy novel");
		source2.setAuthor("장표환");
		source2.setContent("동해물과 백두산이 마르고 닳도록.");
		
		Bulk bulk = new Bulk.Builder()
				.defaultIndex("articles")
				.defaultType("tweet")
				.addAction(new Index.Builder(source).build())
				.addAction(new Index.Builder(source2).build())
				.build();
		
		try {
			jestClient.execute(bulk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	@Ignore
	public void bulk02() {
		
		List<ESBoardFree> posts = jakdukDAO.getBoardFreeOnES(null);
		ESBoardFree lastPost = posts.get(posts.size() - 1);
		
		while (posts.size() > 0) {
			List<Index> idxList = new ArrayList<>();
			
			for (ESBoardFree post : posts) {
				idxList.add(new Index.Builder(post).build());
			}
			
			Bulk bulk = new Bulk.Builder()
					.defaultIndex("articles")
					.defaultType("tweet")
					.addAction(idxList)
					.build();
			
			try {
				JestResult jestResult = jestClient.execute(bulk);
				
				if (!jestResult.isSucceeded()) {
					System.out.println(jestResult.getErrorMessage());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			posts = jakdukDAO.getBoardFreeOnES(new ObjectId(lastPost.getId()));
			if (posts.size() > 0) {
				lastPost = posts.get(posts.size() - 1);
			}
		}
	}

	@Ignore
	@Test
	public void initSearchType() throws JsonProcessingException {
		sut.initSearchType();
	}

	@Ignore
	@Test
	public void initSearchDocuments() {
		sut.initSearchDocuments();
	}


	@Test
	public void queryDSL() throws JsonProcessingException {
		QueryBuilder qb = QueryBuilders.matchQuery("subject", "test");

		System.out.println("phjang=" + qb.toString());

		SearchResponse response = client.prepareSearch("jakduk_test")
				.setQuery(qb)
				.execute()
				.actionGet();

		ObjectMapper objectMapper = new ObjectMapper();

		System.out.println("phjang=" + response.getHits().getTotalHits());
	}

}

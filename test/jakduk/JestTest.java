package jakduk;

import java.io.IOException;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.Article;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 2.
* @desc     :
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class JestTest {
	
	@Autowired
	private JestClient jestClient;
	
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
		
		System.out.println("jestClient=" + jestClient);
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
		
		ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
		settingsBuilder.put("number_of_shards", 5);
		settingsBuilder.put("number_of_replicas", 1);
		settingsBuilder.put("index.analysis.analyzer.korean.type", "custom");
		settingsBuilder.put("index.analysis.analyzer.korean.tokenizer", "mecab_ko_standard_tokenizer");
		
		jestClient.execute(new CreateIndex.Builder("articles").settings(settingsBuilder.build().getAsMap()).build());
	}
	
	@Test
	public void createDocument() {
		Article source = new Article();
		source.setAuthor("John Ronald Reuel Tolkien");
		source.setContent("The Lord of the Rings is an epic high fantasy novel");
		
		Index index = new Index.Builder(source).index("articles").type("tweet").build();
		try {
			jestClient.execute(index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void search() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("author", "Tolkien"));
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex("articles")
				.build();
		
		try {
			SearchResult result = jestClient.execute(search);
			System.out.println("result=" + result.getJsonString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

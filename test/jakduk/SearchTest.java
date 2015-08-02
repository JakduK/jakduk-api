package jakduk;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class SearchTest {

	private Client client;
	
	@Autowired
	static JestClient client02;
	
	@Before
	public void before() {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "jakduk").build();
		
		client = new TransportClient(settings).addTransportAddresses(new InetSocketTransportAddress("localhost", 9300))
				.addTransportAddress(new InetSocketTransportAddress("localhost", 9301));
		
	}
	
    @Test
    public void test03() {
    	SearchResponse response = client.prepareSearch("book")
//    	        .setTypes("type1", "type2")
    	        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
    	        .setQuery(QueryBuilders.termQuery("name", "Data"))             // Query
//    	        .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
//    	        .setFrom(0).setSize(60).setExplain(true)
    	        .execute()
    	        .actionGet();
    	
    	System.out.println(response);
    }
    
    @Test
    public void index01() {
    	try {
			IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
			        .setSource(jsonBuilder()
			                    .startObject()
			                        .field("user", "kimchy")
			                        .field("postDate", new Date())
			                        .field("message", "trying out Elasticsearch")
			                    .endObject()
			                  )
			        .execute()
			        .actionGet();
			
			// Index name
			String _index = response.getIndex();
			// Type name
			String _type = response.getType();
			// Document ID (generated or not)
			String _id = response.getId();
			// Version (if it's the first time you index this document, you will get: 1)
			long _version = response.getVersion();
			// isCreated() is true if the document is a new one, false if it has been updated
			boolean created = response.isCreated();
			
			System.out.println("_index=" + _index);
			System.out.println("_type=" + _type);
			System.out.println("_id=" + _id);
			System.out.println("_version=" + _version);
			System.out.println("created=" + created);
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void get01() {
    	GetResponse response = client.prepareGet("twitter", "tweet", "1")
    	        .execute()
    	        .actionGet();
    	
    	System.out.println("response=" + response.getSourceAsString());
    	
    	Settings set = client.settings();
    	System.out.println("set=" + set.getAsMap());
    	
    }
    
    @Test
    public void jest01() {
System.out.println("client=" + client02);
    }
    
    @After
    public void after() {
    	client.close();
    }

}

package jakduk;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class SearchTest {

	private Client client;
	
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
    
    @After
    public void after() {
    	client.close();
    }

}

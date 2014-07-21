package jakduk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 15.
 * @desc     :
 */
public class SampleJUnits extends TestCase {
	
	public static void main(String[] args) throws SolrServerException, IOException {
		
		/*
		String url = "http://localhost:8983/solr";
		HttpSolrServer server = new HttpSolrServer( url );
		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.addField("id", "id1", 1.0f);
		doc1.addField("name", "doc1", 1.0f);
		doc1.addField("price", 10);

		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(doc1);
		
		server.commit();
		*/
		
		SolrServer server = new HttpSolrServer("http://localhost:8983/solr/");

		SolrQuery solrQuery = new  SolrQuery().
				setQuery("ipod").
				setFacet(true).
				setFacetMinCount(1).
				setFacetLimit(8).
				addFacetField("category").
				addFacetField("inStock");
		QueryResponse rsp = server.query(solrQuery);

		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.addField( "id", "pyohwan", 1.0f );
		doc1.addField( "name", "표환", 1.0f );
		doc1.addField( "price", 32 );

		SolrInputDocument doc2 = new SolrInputDocument();
		doc2.addField( "id", "gwangsu", 1.0f );
		doc2.addField( "name", "광수", 1.0f );
		doc2.addField( "price", 12 );

		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add( doc1 );
		docs.add( doc2 );

		server.add(docs);

		server.commit();

		System.out.println("rsp=" + rsp);
		  
		  
		
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

}

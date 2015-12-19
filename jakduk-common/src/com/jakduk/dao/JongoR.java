package com.jakduk.dao;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.net.UnknownHostException;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 12. 16.
* @desc     :
*/
public class JongoR {
	private Jongo jongo = null;

	public JongoR(String dbName, MongoClient mongoClient) {

		DB db = mongoClient.getDB(dbName);
		jongo = new Jongo(db);
	}

	public Jongo getJongo() {
		return jongo;
	}
}

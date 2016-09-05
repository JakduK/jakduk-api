package com.jakduk.batch;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 9. 5.
 */

//@Configuration
//@EnableMongoRepositories(basePackages = {"com.jakduk.batch.repository"})
public class BatchMongoConfig extends AbstractMongoConfiguration {

	@Resource
	private Environment environment;

	/**
	 * Return the name of the database to connect to.
	 *
	 * @return must not be {@literal null}.
	 */
	@Override protected String getDatabaseName() {
		return environment.getProperty("mongo.db.name");
	}

	/**
	 * Return the {@link Mongo} instance to connect to. Annotate with {@link Bean} in case you want to expose a
	 * {@link Mongo} instance to the {@link ApplicationContext}.
	 *
	 * @return
	 * @throws Exception
	 */
	@Override public Mongo mongo() throws Exception {
		return new MongoClient(environment.getProperty("mongo.host.name"), environment.getProperty("mongo.host.port", Integer.class));
	}
}

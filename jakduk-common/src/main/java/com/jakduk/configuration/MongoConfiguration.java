package com.jakduk.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;

/**
 * Created by pyohwan on 16. 6. 14.
 */

@Configuration
@EnableMongoRepositories(basePackages = {"com.jakduk.repository"})
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Resource
    private Environment environment;

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("mongo.db.name");
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(environment.getProperty("mongo.host.name"), environment.getProperty("mongo.host.port", Integer.class));
    }

    @Bean
    public Jongo jongo() throws Exception {
        return new Jongo(mongo().getDB(getDatabaseName()));
    }
}

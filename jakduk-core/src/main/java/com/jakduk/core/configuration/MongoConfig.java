package com.jakduk.core.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;

/**
 * @author pyohwan
 * 16. 6. 14 오후 11:35
 */

@Configuration
@EnableMongoRepositories(basePackages = {"com.jakduk.core.repository"})
public class MongoConfig extends AbstractMongoConfiguration {

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

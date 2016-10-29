package com.jakduk.core.configuration;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pyohwan on 16. 10. 29.
 */

@Configuration
@ConditionalOnClass({Jongo.class})
public class JongoAutoConfig {

    @Autowired
    protected Mongo mongo;

    @Autowired
    protected MongoProperties properties;

    @Bean
    public Jongo jongo() {
        final DB database = mongo.getDB(properties.getDatabase());
        return new Jongo(database);
    }
}

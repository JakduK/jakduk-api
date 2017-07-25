package com.jakduk.api.configuration;

import com.jakduk.api.common.converter.DateToLocalDateTimeConverter;
import com.jakduk.api.common.converter.LocalDateTimeToDateConverter;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.apache.commons.lang3.StringUtils;
import org.jongo.Jongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author pyohwan
 * 16. 6. 14 오후 11:35
 */

@Configuration
@EnableMongoRepositories("com.jakduk.api.repository")
public class MongodbConfig extends AbstractMongoConfiguration {

    @Resource private JakdukProperties.Mongodb mongodbProperties;

    @Override
    protected String getDatabaseName() {
        return mongodbProperties.getDatabase();
    }

    @Override
    public Mongo mongo() throws Exception {

        List<ServerAddress> seeds = Stream.of(StringUtils.split(mongodbProperties.getHost(), ","))
                .map(hostName -> {
                    try {
                        URL url = new URL(hostName);
                        return new ServerAddress(url.getHost(), url.getPort());
                    } catch (MalformedURLException e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        return new MongoClient(seeds);
    }

    @Bean
    public Jongo jongo() throws Exception {
        return new Jongo(mongo().getDB(getDatabaseName()));
    }

    @Bean
    @Override
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new DateToLocalDateTimeConverter());
        converters.add(new LocalDateTimeToDateConverter());
        return new CustomConversions(converters);
    }

}

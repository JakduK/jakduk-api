package com.jakduk.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by pyohwan on 16. 10. 19.
 */

@SpringBootApplication
@PropertySource("classpath:/application-core.properties")
@EnableMongoRepositories(basePackages = {"com.jakduk.core.repository"})
public class CoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }
}

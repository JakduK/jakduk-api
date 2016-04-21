package com.jakduk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by pyohwan on 16. 4. 21.
 */

@Configuration
@Profile("production")
@PropertySource({"classpath:/config/jakduk.properties", "classpath:/config/profile-production.properties"})
public class ProductionPropertySourceConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

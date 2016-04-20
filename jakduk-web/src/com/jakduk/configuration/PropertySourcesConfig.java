package com.jakduk.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Created by pyohwan on 16. 4. 2.
 */

@Configuration
@Slf4j
public class PropertySourcesConfig {

    @Configuration
    @Profile("local")
    @PropertySource({"classpath:/config/jakduk.properties", "classpath:/config/profile-local.properties"})
    static class Defaults
    {}

    @Configuration
    @Profile("production")
    @PropertySource({"classpath:/config/jakduk.properties", "classpath:/config/profile-production.properties"})
    static class Overrides
    {}

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}

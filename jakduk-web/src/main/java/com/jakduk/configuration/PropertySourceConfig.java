package com.jakduk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author pyohwan
 * 16. 4. 21 오후 10:28
 */

@Configuration
@PropertySource({"classpath:/config/jakduk.properties", "classpath:/config/application-${spring.profiles.active}.properties"})
public class PropertySourceConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

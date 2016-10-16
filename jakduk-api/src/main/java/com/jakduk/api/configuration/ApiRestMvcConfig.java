package com.jakduk.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author pyohwan
 * 16. 5. 25 오후 10:49
 */

//@EnableWebMvc
@Configuration
public class ApiRestMvcConfig extends RepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);

        config.setBasePath("/rest");
    }
}

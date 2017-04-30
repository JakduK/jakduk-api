package com.jakduk.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pyohwan
 * 16. 6. 7 오후 11:45
 */

@Configuration
@EnableSwagger2
public class ApiSwaggerConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Docket api() {

        Set<String> protocols = new HashSet<>();
        protocols.add(environment.getProperty("swagger.protocol"));

        Set<String> producesList = new HashSet<>();
        producesList.add("application/json");

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.jakduk.restcontroller"))
                    .paths(PathSelectors.ant("/api/**"))
                .build()
                .protocols(protocols)
                .host(environment.getProperty("swagger.host"))
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(Collections.singletonList(apiKey()))
                .produces(producesList);
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(
                "test-app-client-id",
                "test-app-client-secret",
                "test-app-realm",
                "test-app",
                "",
                ApiKeyVehicle.HEADER,
                "api_key",
                "," /*scope separator*/);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JakduK REST API with Swagger")
                .contact(new Contact("pio.", "https://jakduk.com", "phjang1983@daum.net"))
                .license("The MIT License (MIT)")
                .licenseUrl("https://github.com/JakduK/jakduk-api/blob/master/LICENSE")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JSESSIONID", "api_key", "cookie");
    }
}

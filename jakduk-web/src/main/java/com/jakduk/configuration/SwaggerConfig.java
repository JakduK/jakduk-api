package com.jakduk.configuration;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author pyohwan
 * 16. 6. 7 오후 11:45
 */

@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.jakduk.restcontroller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "JakduK REST API",
                "Some custom description of API.",
                "API TOS",
                "Terms of service",
                new Contact("pio.", "https://jakduk.com", "phjang1983@daum.net"),
                "License of API",
                "https://github.com/JakduK/JakduK/blob/master/LICENSE");
        return apiInfo;
    }
}

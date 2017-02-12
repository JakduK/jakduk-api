package com.jakduk.core;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by pyohwan on 16. 10. 19.
 */

@SpringBootApplication
@PropertySource("classpath:/application-core.properties")
public class CoreApplication {

    @Resource
    private Environment environment;

    public static void main(String[] args) {
        new SpringApplicationBuilder(CoreApplication.class)
                .web(false)
                .profiles("core-default")
                .run(args);
    }

    @Bean
    public SlackApi slackApi() {
        return new SlackApi(environment.getProperty("core.slack.board.webhook"));
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:messages/common", "classpath:messages/board", "classpath:messages/user",
                "classpath:messages/about", "classpath:messages/home", "classpath:messages/gallery",
                "classpath:messages/search", "classpath:messages/jakdu", "classpath:messages/email",
                "classpath:messages/exception"
        );

        return messageSource;
    }
}

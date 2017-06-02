package com.jakduk.core;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * Created by pyohwan on 16. 10. 19.
 */

@SpringBootApplication
@PropertySource("classpath:/application-core.properties")
@EnableScheduling
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
                "messages/common", "messages/board", "messages/user", "messages/about", "messages/home",
                "messages/gallery", "messages/search", "messages/jakdu", "messages/email", "messages/exception"
        );

        return messageSource;
    }

    @Profile("usage_message")
    @Bean
    public CommandLineRunner usage() {
        return new CommandLineRunner() {

            @Override
            public void run(String... arg0) throws Exception {
                System.out.println("This app uses Spring Profiles to control its behavior.\n");
                        System.out.println("Sample usage: java -jar rabbit-tutorials.jar --spring.profiles.active=hello-world,sender");
            }
        };
    }

    @Profile("!usage_message")
    @Bean
    public CommandLineRunner tutorial() {
        return new RabbitAmqpTutorialsRunner();
    }

}

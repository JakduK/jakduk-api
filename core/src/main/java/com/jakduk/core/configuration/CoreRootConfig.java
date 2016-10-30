package com.jakduk.core.configuration;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author pyohwan
 *         16. 8. 1 오후 10:40
 */

@Configuration
@ComponentScan(basePackages = {"com.jakduk.core"},
        excludeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
public class CoreRootConfig {

    @Resource
    private Environment environment;

    @Bean
    public SlackApi slackApi() {
        return new SlackApi(environment.getProperty("slack.board.webhook"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/common", "classpath:messages/board", "classpath:messages/user",
                "classpath:messages/about", "classpath:messages/home", "classpath:messages/gallery",
                "classpath:messages/stats", "classpath:messages/search", "classpath:messages/jakdu", "classpath:messages/email",
                "classpath:messages/exception");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(180);

        return messageSource;
    }
}

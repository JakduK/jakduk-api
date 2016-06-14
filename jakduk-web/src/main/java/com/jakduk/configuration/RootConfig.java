package com.jakduk.configuration;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * applicationContext.xml
 * Created by pyohwan on 16. 4. 2.
 */

@Configuration
@ComponentScan(basePackages = {"com.jakduk"}, excludeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
public class RootConfig {

    @Resource
    private Environment environment;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("smtp.host"));
        mailSender.setPort(environment.getProperty("smtp.port", Integer.class));
        mailSender.setUsername(environment.getProperty("smtp.username"));
        mailSender.setPassword(environment.getProperty("smtp.password"));
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.starttls.enable", true);
        mailProperties.put("mail.smtp.ssl.trust", environment.getProperty("smtp.host"));
        mailSender.setJavaMailProperties(mailProperties);

        return mailSender;
    }

    @Bean
    public VelocityEngineFactoryBean velocityEngine() {
        VelocityEngineFactoryBean velocityEngine = new VelocityEngineFactoryBean();
        velocityEngine.setResourceLoaderPath("classpath:/templates/email");

        return velocityEngine;
    }

    @Bean
    public SlackApi slackApi() {
        return new SlackApi(environment.getProperty("slack.board.webhook"));
    }
}

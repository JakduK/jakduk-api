package com.jakduk.core.configuration;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author pyohwan
 *         16. 8. 1 오후 10:40
 */

@Configuration
@ComponentScan(basePackages = {"com.jakduk.core"},
        excludeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
@PropertySource({"classpath:/properties/core.properties",
        "classpath:/properties/core-${spring.profiles.active}.properties"})
public class CoreRootConfig {

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
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:templates/email/");
        configurer.setDefaultEncoding("UTF-8");

        return configurer;
    }

    @Bean
    public SlackApi slackApi() {
        return new SlackApi(environment.getProperty("slack.board.webhook"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }
}

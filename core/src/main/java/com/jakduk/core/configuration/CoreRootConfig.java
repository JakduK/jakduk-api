package com.jakduk.core.configuration;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.annotation.Resource;
import java.util.Properties;

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
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(180);

        return messageSource;
    }

    /**
     * THYMELEAF: Template Engine (Spring4-specific version) for HTML email templates.
     */
    @Bean
    public TemplateEngine htmlTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    /**
     * THYMELEAF: Template Resolver for HTML email templates.
     */
    private ITemplateResolver htmlTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}

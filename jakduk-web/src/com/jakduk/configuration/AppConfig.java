package com.jakduk.configuration;

import com.jakduk.dao.JongoR;
import com.jakduk.model.db.Token;
import com.jakduk.trigger.TokenTerminationTrigger;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineFactory;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.net.UnknownHostException;
import java.util.Properties;


/**
 * Created by pyohwan on 16. 4. 2.
 */

@Configuration
@ComponentScan(basePackages = {"com.jakduk"}, excludeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
@EnableMongoRepositories(basePackages = {"com.jakduk.repository"})
public class AppConfig {

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

    /*
     * Use the standard Mongo driver API to create a com.mongodb.Mongo instance.
     */
    @Bean
    public Mongo mongo() throws UnknownHostException {
        return new Mongo(environment.getProperty("mongo.host.name"), environment.getProperty("mongo.host.port", Integer.class));
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(mongo(), environment.getProperty("mongo.db.name"));
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), environment.getProperty("mongo.db.name"));
    }

    @Bean
    public JongoR jongoR() throws UnknownHostException {
        return new JongoR(environment.getProperty("mongo.db.name"), mongoClient());
    }

    @Bean
    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient(environment.getProperty("mongo.host.name"), environment.getProperty("mongo.host.port", Integer.class));
    }

}

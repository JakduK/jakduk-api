package com.jakduk.configuration;

import net.exacode.spring.social.connect.GenericUsersConnectionRepository;
import net.exacode.spring.social.connect.mongo.MongoConnectionConverter;
import net.exacode.spring.social.connect.mongo.MongoConnectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.daum.connect.DaumConnectionFactory;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

/**
 * @author pyohwan
 * 16. 4. 8 오후 10:21
 */

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    @Autowired
    private Environment environment;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(facebookConnectionFactory());
        connectionFactoryConfigurer.addConnectionFactory(daumConnectionFactory());
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        MongoConnectionDao mongoConnectionDao = new MongoConnectionDao(mongoTemplate, mongoConnectionConverter(connectionFactoryLocator));

        return new GenericUsersConnectionRepository(mongoConnectionDao, connectionFactoryLocator);
    }

    @Bean
    public FacebookConnectionFactory facebookConnectionFactory() {
        return new FacebookConnectionFactory(environment.getProperty("facebook.app.id"), environment.getProperty("facebook.app.secret"));
    }

    @Bean
    public DaumConnectionFactory daumConnectionFactory() {
        return new DaumConnectionFactory(environment.getProperty("daum.client.id"), environment.getProperty("daum.client.secret"));
    }
    /**
     * This bean manages the connection flow between the account provider and
     * the example application.
     */
    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {

        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
    }

    @Bean
    public MongoConnectionConverter mongoConnectionConverter(ConnectionFactoryLocator connectionFactoryLocator) {
        return new MongoConnectionConverter(connectionFactoryLocator, Encryptors.noOpText());
    }

}

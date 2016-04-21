package com.jakduk.configuration;

import com.jakduk.configuration.mongo.MongoConnectionDao;
import net.exacode.spring.social.connect.GenericUsersConnectionRepository;
import net.exacode.spring.social.connect.mongo.MongoConnectionConverter;
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
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.daum.connect.DaumConnectionFactory;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

/**
 * Created by pyohwan on 16. 4. 8.
 */

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.app.id"), environment.getProperty("facebook.app.secret")));
        connectionFactoryConfigurer.addConnectionFactory(new DaumConnectionFactory(environment.getProperty("daum.client.id"), environment.getProperty("daum.client.secret")));
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

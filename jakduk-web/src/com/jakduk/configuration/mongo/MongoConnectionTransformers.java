package com.jakduk.configuration.mongo;

import com.google.common.base.Function;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;

import java.util.Date;

public class MongoConnectionTransformers {

    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final TextEncryptor textEncryptor;

    public MongoConnectionTransformers(final ConnectionFactoryLocator connectionFactoryLocator, final TextEncryptor textEncryptor) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
    }

    public Function<MongoConnection, String> toUserId() {
        return new Function<MongoConnection, String>() {
            @Override
            public String apply(final MongoConnection input) {
                if (input == null) {
                    return null;
                }
                return input.getUserId();
            }
        };
    }
    
    public Function<MongoConnection, Connection<?>> toConnection() {
        return new Function<MongoConnection, Connection<?>>() {
            @Override
            public Connection<?> apply(final MongoConnection input) {
                if (input == null) {
                    return null;
                }
                final ConnectionData cd = new ConnectionData(
                        input.getProviderId(),
                        input.getProviderUserId(),
                        input.getDisplayName(),
                        input.getProfileUrl(),
                        input.getImageUrl(),
                        decrypt(input.getAccessToken()),
                        decrypt(input.getSecret()),
                        decrypt(input.getRefreshToken()),
                        input.getExpireTime()
                );
                final ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(input.getProviderId());
                return connectionFactory.createConnection(cd);
            }
        };
    }
    
    public Function<Connection<?>, MongoConnection> fromConnection(final String userId) {
        return new Function<Connection<?>, MongoConnection>() {
            @Override
            public MongoConnection apply(final Connection<?> input) {
                if (input == null) {
                    return null;
                }
                final ConnectionData cd = input.createData();
                final MongoConnection mongoConnection = new MongoConnection();
                mongoConnection.setCreated(new Date());
                mongoConnection.setUserId(userId);
                mongoConnection.setProviderId(cd.getProviderId());
                mongoConnection.setProviderUserId(cd.getProviderUserId());
                mongoConnection.setDisplayName(cd.getDisplayName());
                mongoConnection.setProfileUrl(cd.getProfileUrl());
                mongoConnection.setImageUrl(cd.getImageUrl());
                mongoConnection.setAccessToken(encrypt(cd.getAccessToken()));
                mongoConnection.setSecret(encrypt(cd.getSecret()));
                mongoConnection.setRefreshToken(encrypt(cd.getRefreshToken()));
                mongoConnection.setExpireTime(cd.getExpireTime());
                return mongoConnection;
            }
        };
    }
    
    private String encrypt(final String decrypted) {
        if (decrypted == null) {
            return null;
        }
        return textEncryptor.encrypt(decrypted);
    }
    
    private String decrypt(final String encrypted) {
        if (encrypted == null) {
            return null;
        }
        return textEncryptor.decrypt(encrypted);
    }
}

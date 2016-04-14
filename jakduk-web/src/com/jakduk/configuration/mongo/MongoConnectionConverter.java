package com.jakduk.configuration.mongo;

import net.exacode.spring.social.connect.mongo.MongoConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

/**
 * Converts data between spring social connections {@link Connection} and
 * MongoDB documents {@link MongoConnection}.
 * 
 * @author mendlik
 */
@Component
public class MongoConnectionConverter {
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final TextEncryptor textEncryptor;

	@Autowired
	public MongoConnectionConverter(
			ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor) {
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	public Connection<?> convert(MongoConnection cnn) {
		if (cnn == null)
			return null;

		ConnectionData connectionData = fillConnectionData(cnn);
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator
				.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}

	private ConnectionData fillConnectionData(MongoConnection uc) {
		return new ConnectionData(uc.getProviderId(), uc.getProviderUserId(),
				uc.getDisplayName(), uc.getProfileUrl(), uc.getImageUrl(),
				decrypt(uc.getAccessToken()), decrypt(uc.getSecret()),
				decrypt(uc.getRefreshToken()), uc.getExpireTime());
	}

	public MongoConnection convert(Connection<?> cnn) {
		ConnectionData data = cnn.createData();
		MongoConnection userConn = new MongoConnection();
		userConn.setProviderId(data.getProviderId());
		userConn.setProviderUserId(data.getProviderUserId());
		userConn.setDisplayName(data.getDisplayName());
		userConn.setProfileUrl(data.getProfileUrl());
		userConn.setImageUrl(data.getImageUrl());
		userConn.setAccessToken(encrypt(data.getAccessToken()));
		userConn.setSecret(encrypt(data.getSecret()));
		userConn.setRefreshToken(encrypt(data.getRefreshToken()));
		userConn.setExpireTime(data.getExpireTime());
		return userConn;
	}

	private String decrypt(String encryptedText) {
		return encryptedText != null ? textEncryptor.decrypt(encryptedText)
				: encryptedText;
	}

	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}
}

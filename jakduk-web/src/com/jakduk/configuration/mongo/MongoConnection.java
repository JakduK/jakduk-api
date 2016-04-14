package com.jakduk.configuration.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The MongoDB collection for the spring social connections.
 * <p>
 * Note: After conversion to {@link net.exacode.spring.social.connect.mongo.MongoConnection} all OAuth tokens are
 * encrypted.
 * 
 * 
 * @author mendlik
 */
@Document(collection = "connections")
@CompoundIndexes({
		@CompoundIndex(name = "connectionsRankIdx", def = "{'userId': 1, 'providerId': 1, 'rank': 1}", unique = true),
		@CompoundIndex(name = "connectionsPrimaryIdx", def = "{'userId': 1, 'providerId': 1, 'providerUserId': 1}", unique = true) })
public class MongoConnection {
	@Id
	private ObjectId id;
	private String userId;
	private String providerId;
	private String providerUserId;
	private int rank;
	private String displayName;
	private String profileUrl;
	private String imageUrl;
	private String accessToken;
	private String secret;
	private String refreshToken;
	private Long expireTime;

	public ObjectId getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}
}
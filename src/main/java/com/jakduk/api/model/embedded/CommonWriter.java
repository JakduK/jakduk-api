package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;

/**
 * 공통으로 사용하는 작성자
 */

public class CommonWriter {
	
	private String userId;
	private String username;
	private Constants.ACCOUNT_TYPE providerId;
	private UserPictureInfo picture;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Constants.ACCOUNT_TYPE getProviderId() {
		return providerId;
	}

	public void setProviderId(Constants.ACCOUNT_TYPE providerId) {
		this.providerId = providerId;
	}

	public UserPictureInfo getPicture() {
		return picture;
	}

	public void setPicture(UserPictureInfo picture) {
		this.picture = picture;
	}
}

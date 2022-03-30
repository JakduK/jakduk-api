package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.Constants;

/**
 * SNS로 회원 가입시 임시로 회원 정보를 담는 객체
 *
 * @author pyohwan
 *         16. 7. 30 오후 9:54
 */

public class AttemptSocialUser {

	private String email;
	private String username;
	private Constants.ACCOUNT_TYPE providerId;
	private String providerUserId;
	private String externalSmallPictureUrl;
	private String externalLargePictureUrl;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public String getExternalSmallPictureUrl() {
		return externalSmallPictureUrl;
	}

	public void setExternalSmallPictureUrl(String externalSmallPictureUrl) {
		this.externalSmallPictureUrl = externalSmallPictureUrl;
	}

	public String getExternalLargePictureUrl() {
		return externalLargePictureUrl;
	}

	public void setExternalLargePictureUrl(String externalLargePictureUrl) {
		this.externalLargePictureUrl = externalLargePictureUrl;
	}
}

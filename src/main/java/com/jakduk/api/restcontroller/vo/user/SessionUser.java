package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.UserPictureInfo;

import java.util.List;

/**
 * 인증된 회원의 프로필 정보를 담는 객체
 *
 * @author pyohwan
 *         16. 7. 14 오전 12:21
 */

public class SessionUser {

	private String id;
	private String email;
	private String username;
	private Constants.ACCOUNT_TYPE providerId;
	private List<String> roles;
	private UserPictureInfo picture;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public UserPictureInfo getPicture() {
		return picture;
	}

	public void setPicture(UserPictureInfo picture) {
		this.picture = picture;
	}
}

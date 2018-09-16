package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class User {

	@Id
	private String id;
	private String email;							// 이메일
	private String username;						// 별명
	private String password;						// 비밀번호
	private Constants.ACCOUNT_TYPE providerId;		// 제공자
	private String providerUserId;					// SNS USER ID
	private String about;							// 소개
	@DBRef
	private FootballClub supportFC;					// 지지구단
	@DBRef
	private UserPicture userPicture;				// 프로필 사진
    private List<Integer> roles;					// 권한
	private LocalDateTime lastLogged;				// 마지막으로 로그인한 날짜

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public FootballClub getSupportFC() {
		return supportFC;
	}

	public void setSupportFC(FootballClub supportFC) {
		this.supportFC = supportFC;
	}

	public UserPicture getUserPicture() {
		return userPicture;
	}

	public void setUserPicture(UserPicture userPicture) {
		this.userPicture = userPicture;
	}

	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}

	public LocalDateTime getLastLogged() {
		return lastLogged;
	}

	public void setLastLogged(LocalDateTime lastLogged) {
		this.lastLogged = lastLogged;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", email='" + email + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", providerId=" + providerId +
				", providerUserId='" + providerUserId + '\'' +
				", about='" + about + '\'' +
				", supportFC=" + supportFC +
				", userPicture=" + userPicture +
				", roles=" + roles +
				", lastLogged=" + lastLogged +
				'}';
	}
}

package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistUsername;
import com.jakduk.api.common.constraint.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 이메일 기반 회원 가입 폼
 *
 * @author pyohwan
 * 16. 6. 26 오후 1:12
 */

@FieldMatch(first = "password", second = "passwordConfirm", message = "{validation.msg.password.mismatch}")
public class UserForm {

	@Size(min = 6, max = 30)
	@NotEmpty
	@Email
	@ExistEmail
	private String email;

	@Size(min = 2, max = 20)
	@NotEmpty
	@ExistUsername
	private String username;

	@Size(min = 4, max = 20)
	@NotEmpty
	private String password;

	@Size(min = 4, max = 20)
	@NotEmpty
	private String passwordConfirm;

	private String about; // 자기 소개
	private String footballClub; // 축구단(FootballClub) ID
	private String userPictureId; // UserPicture의 ID

	public UserForm() {
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

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getFootballClub() {
		return footballClub;
	}

	public void setFootballClub(String footballClub) {
		this.footballClub = footballClub;
	}

	public String getUserPictureId() {
		return userPictureId;
	}

	public void setUserPictureId(String userPictureId) {
		this.userPictureId = userPictureId;
	}
}

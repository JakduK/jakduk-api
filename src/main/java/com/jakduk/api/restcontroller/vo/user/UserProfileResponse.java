package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.model.embedded.UserPictureInfo;

/**
 * 회원 프로필 응답 객체
 *
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     : 회원 정보 열람.
 */

public class UserProfileResponse {
	private String email; // 이메일 주소
	private String username; // 별명
	private String about; // 소개
	private Constants.ACCOUNT_TYPE providerId; // provider ID
	private LocalName footballClubName; // 지지하는 축구단
	private UserPictureInfo picture; // 회원 사진 객체
	private Boolean temporaryEmail; // 임시로 발급한 이메일인지 여부

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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Constants.ACCOUNT_TYPE getProviderId() {
		return providerId;
	}

	public void setProviderId(Constants.ACCOUNT_TYPE providerId) {
		this.providerId = providerId;
	}

	public LocalName getFootballClubName() {
		return footballClubName;
	}

	public void setFootballClubName(LocalName footballClubName) {
		this.footballClubName = footballClubName;
	}

	public UserPictureInfo getPicture() {
		return picture;
	}

	public void setPicture(UserPictureInfo picture) {
		this.picture = picture;
	}

	public Boolean getTemporaryEmail() {
		return temporaryEmail;
	}

	public void setTemporaryEmail(Boolean temporaryEmail) {
		this.temporaryEmail = temporaryEmail;
	}
}

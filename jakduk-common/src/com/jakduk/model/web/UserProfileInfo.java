package com.jakduk.model.web;

import com.jakduk.model.embedded.FootballClubName;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     :
 */
public class UserProfileInfo {

	private String email;
	
	private String username;
	
	private String about;
	
	private FootballClubName footballClubName;

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

	public FootballClubName getFootballClubName() {
		return footballClubName;
	}

	public void setFootballClubName(FootballClubName footballClubName) {
		this.footballClubName = footballClubName;
	}

	@Override
	public String toString() {
		return "UserProfileInfo [email=" + email + ", username=" + username
				+ ", about=" + about + ", footballClubName=" + footballClubName
				+ "]";
	}	
}

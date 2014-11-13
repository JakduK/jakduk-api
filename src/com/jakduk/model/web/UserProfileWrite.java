package com.jakduk.model.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     :
 */
public class UserProfileWrite {
	
	@NotNull
	@Size(min = 6, max=30)
	private String email;
	
	@NotNull
	@Size(min = 2, max=20)
	private String username;
	
	private String about;
	
	private String footballClub;
	
	private String usernameStatus = "none";

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

	public String getFootballClub() {
		return footballClub;
	}

	public void setFootballClub(String footballClub) {
		this.footballClub = footballClub;
	}
	
	public String getUsernameStatus() {
		return usernameStatus;
	}

	public void setUsernameStatus(String usernameStatus) {
		this.usernameStatus = usernameStatus;
	}

	@Override
	public String toString() {
		return "UserProfileWrite [email=" + email + ", username=" + username
				+ ", about=" + about + ", footballClub=" + footballClub
				+ ", usernameStatus=" + usernameStatus + "]";
	}

}

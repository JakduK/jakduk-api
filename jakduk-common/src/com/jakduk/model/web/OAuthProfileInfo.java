package com.jakduk.model.web;

import com.jakduk.model.embedded.LocalName;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 2.
 * @desc     :
 */
public class OAuthProfileInfo {
	
	private String username;
	
	private String about;
	
	private LocalName footballClubName;

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

	public LocalName getFootballClubName() {
		return footballClubName;
	}

	public void setFootballClubName(LocalName footballClubName) {
		this.footballClubName = footballClubName;
	}

	@Override
	public String toString() {
		return "OAuthProfileInfo [username=" + username + ", about=" + about
				+ ", footballClubName=" + footballClubName + "]";
	}

}

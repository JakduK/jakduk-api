package com.jakduk.model.web;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 2.
 * @desc     :
 */
public class OAuthUserWrite {

	private String about;
	
	@DBRef
	private FootballClub footballClub;

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public FootballClub getFootballClub() {
		return footballClub;
	}

	public void setFootballClub(FootballClub footballClub) {
		this.footballClub = footballClub;
	}

	@Override
	public String toString() {
		return "OAuthUserWrite [about=" + about + ", footballClub="
				+ footballClub + "]";
	}

}

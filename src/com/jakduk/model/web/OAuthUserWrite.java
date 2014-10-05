package com.jakduk.model.web;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 2.
 * @desc     :
 */
public class OAuthUserWrite {

	private String about;
	
	private String footballClub;
	
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

	@Override
	public String toString() {
		return "OAuthUserWrite [about=" + about + ", footballClub="
				+ footballClub + "]";
	}

}

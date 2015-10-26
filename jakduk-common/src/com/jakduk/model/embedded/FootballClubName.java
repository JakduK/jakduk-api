package com.jakduk.model.embedded;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 17.
 * @desc     :
 */

@Document
public class FootballClubName {

	private String language;
	
	private String fullName;
	
	private String shortName;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return "FootballClubName [language=" + language + ", fullName="
				+ fullName + ", shortName=" + shortName + "]";
	}
	
}

package com.jakduk.model.simple;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 6.
 * @desc     :
 */

@Document(collection = "user")
public class UserProfile {
	
	private String id;
	
	private String email;
	
	private String username;
	
	private List<String> rules;
	
	private String about;
	
	@DBRef
	private FootballClub supportFC;

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

	public List<String> getRules() {
		return rules;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
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

	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", email=" + email + ", username="
				+ username + ", rules=" + rules + ", about=" + about
				+ ", supportFC=" + supportFC + "]";
	}

}

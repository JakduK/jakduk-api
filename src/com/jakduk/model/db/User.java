package com.jakduk.model.db;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.simple.OAuthUser;

@Document
public class User {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String email;
	
	private String username;
	
	private String password;
	
	private List<String> rules;
	
	@Temporal(TemporalType.DATE)
	private Date joined;
	
	private String about;
	
	private OAuthUser oauthUser;

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

	public List<String> getRules() {
		return rules;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
	
	public OAuthUser getOauthUser() {
		return oauthUser;
	}

	public void setOauthUser(OAuthUser oauthUser) {
		this.oauthUser = oauthUser;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", username=" + username
				+ ", password=" + password + ", rules=" + rules + ", joined="
				+ joined + ", about=" + about + ", oauthUser=" + oauthUser
				+ "]";
	}
	
}

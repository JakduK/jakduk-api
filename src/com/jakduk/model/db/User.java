package com.jakduk.model.db;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.OAuthUser;

@Document
public class User {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String email;
	
	private String username;
	
	private String password;
	
	private OAuthUser oauthUser;
	
	private List<Integer> rules;
	
	@Temporal(TemporalType.DATE)
	private Date joined;
	
	private String about;
	
	@DBRef
	private FootballClub supportFC;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FootballClub getSupportFC() {
		return supportFC;
	}

	public void setSupportFC(FootballClub supportFC) {
		this.supportFC = supportFC;
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

	public List<Integer> getRules() {
		return rules;
	}

	public void setRules(List<Integer> rules) {
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
				+ ", password=" + password + ", oauthUser=" + oauthUser
				+ ", rules=" + rules + ", joined=" + joined + ", about="
				+ about + ", supportFC=" + supportFC + "]";
	}
	
}

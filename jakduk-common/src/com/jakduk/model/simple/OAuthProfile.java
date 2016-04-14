package com.jakduk.model.simple;

import java.util.List;

import com.jakduk.model.embedded.SocialInfo;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 2.
 * @desc     :
 */

@Document(collection = "user")
public class OAuthProfile {
	
	private String id;
	
	private String username;
	
	private SocialInfo oauthUser;
	
	private List<Integer> roles;
	
	private String about;
	
	@DBRef
	private FootballClub supportFC;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public SocialInfo getOauthUser() {
		return oauthUser;
	}

	public void setOauthUser(SocialInfo oauthUser) {
		this.oauthUser = oauthUser;
	}
	
	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
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
		return "OAuthProfile [id=" + id + ", username=" + username
				+ ", socialInfo=" + oauthUser + ", roles=" + roles + ", about="
				+ about + ", supportFC=" + supportFC + "]";
	}

}

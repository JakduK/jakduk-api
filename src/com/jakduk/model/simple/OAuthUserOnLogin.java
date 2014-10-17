package com.jakduk.model.simple;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.OAuthUser;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 12.
 * @desc     :
 */

@Document(collection = "user")
public class OAuthUserOnLogin {
	
	@Id
	private String id;
	
	private String username;
	
	private OAuthUser oauthUser;

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

	public OAuthUser getOauthUser() {
		return oauthUser;
	}

	public void setOauthUser(OAuthUser oauthUser) {
		this.oauthUser = oauthUser;
	}

	@Override
	public String toString() {
		return "OAuthUserOnLogin [id=" + id + ", username=" + username
				+ ", oauthUser=" + oauthUser + "]";
	}

}

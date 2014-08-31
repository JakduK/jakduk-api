package com.jakduk.model.simple;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 12.
 * @desc     :
 */

@Document(collection = "user")
public class OAuthUserWrite {
	
	@Id
	private String id;
	
	private OAuthUser oauthUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OAuthUser getOauthUser() {
		return oauthUser;
	}

	public void setOauthUser(OAuthUser oauthUser) {
		this.oauthUser = oauthUser;
	}

	@Override
	public String toString() {
		return "OAuthUserWrite [id=" + id + ", oauthUser=" + oauthUser + "]";
	}

}

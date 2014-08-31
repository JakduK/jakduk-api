package com.jakduk.model.simple;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 31.
 * @desc     :
 */

@Document
public class OAuthUser {
	
	private Integer type;
	
	private String oauthId;
	
	private Integer addInfoStatus = 0;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOauthId() {
		return oauthId;
	}

	public void setOauthId(String oauthId) {
		this.oauthId = oauthId;
	}

	public Integer getAddInfoStatus() {
		return addInfoStatus;
	}

	public void setAddInfoStatus(Integer addInfoStatus) {
		this.addInfoStatus = addInfoStatus;
	}

	@Override
	public String toString() {
		return "OAuthUser [type=" + type + ", oauthId=" + oauthId
				+ ", addInfoStatus=" + addInfoStatus + "]";
	}

}

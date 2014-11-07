package com.jakduk.model.embedded;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.common.CommonConst;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 31.
 * @desc     :
 */

@Document
public class OAuthUser {
	
	/**
	 * OAuth 프로바이더 종류이다. CommonConst클래스의 OAUTH_TYPE_xxx 와 대응된다.
	 */
	private String type;
	
	private String oauthId;
	
	private String addInfoStatus = CommonConst.OAUTH_ADDITIONAL_INFO_STATUS_UNUSE;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOauthId() {
		return oauthId;
	}

	public void setOauthId(String oauthId) {
		this.oauthId = oauthId;
	}
	
	public String getAddInfoStatus() {
		return addInfoStatus;
	}

	public void setAddInfoStatus(String addInfoStatus) {
		this.addInfoStatus = addInfoStatus;
	}

	@Override
	public String toString() {
		return "OAuthUser [type=" + type + ", oauthId=" + oauthId
				+ ", addInfoStatus=" + addInfoStatus + "]";
	}

}

package com.jakduk.api.model.embedded;

import org.springframework.data.annotation.Id;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 6. 16.
 * @desc     : 게시판의 좋아요, 싫어요 등을 사용하는 사용자
 */

public class CommonFeelingUser {

	@Id
	private String id;
	private String userId;
	private String username;

	public CommonFeelingUser() {
	}

	public CommonFeelingUser(String id, String userId, String username) {
		this.id = id;
		this.userId = userId;
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

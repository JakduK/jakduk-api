package com.jakduk.model.embedded;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 게시판 작성자
 * @author pyohwan
 *
 */

@Document
public class CommonWriter {
	
	private String userId;
	
	private String username;
	
	private String type;
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "BoardWriter [userId=" + userId + ", username=" + username
				+ ", type=" + type + "]";
	}

}

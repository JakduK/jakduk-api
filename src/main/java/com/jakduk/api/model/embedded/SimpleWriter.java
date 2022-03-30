package com.jakduk.api.model.embedded;

public class SimpleWriter {
	private String userId;
	private String username;

	public SimpleWriter() {
	}

	public SimpleWriter(String userId, String username) {
		this.userId = userId;
		this.username = username;
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

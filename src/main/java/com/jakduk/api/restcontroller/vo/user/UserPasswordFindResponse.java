package com.jakduk.api.restcontroller.vo.user;

public class UserPasswordFindResponse {
	private String subject;
	private String message;

	public UserPasswordFindResponse() {
	}

	public UserPasswordFindResponse(String subject, String message) {
		this.subject = subject;
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}
}

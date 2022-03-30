package com.jakduk.api.restcontroller.vo.user;

import javax.validation.constraints.NotEmpty;

public class UserPasswordFindForm {

	@NotEmpty
	private String email;

	@NotEmpty
	private String callbackUrl;

	public String getEmail() {
		return email;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}
}

package com.jakduk.model.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     : 회원 가입시에만 사용되는 모델. USER 모델로 바인딩 한다.
 */
public class UserWrite {

	@NotNull
	@Size(min = 6, max=20)
	private String email;
	
	@NotNull
	@Size(min = 2, max=20)
	private String username;
	
	@NotNull
	@Size(min = 4, max=20)
	private String password;
	
	@NotNull
	@Size(min = 4, max=20)
	private String passwordConfirm;
	
	private String about;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	@Override
	public String toString() {
		return "UserWrite [email=" + email + ", username=" + username
				+ ", password=" + password + ", passwordConfirm="
				+ passwordConfirm + ", about=" + about + "]";
	}

}

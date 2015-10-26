package com.jakduk.model.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     :
 */
public class UserPasswordUpdate {

	@NotNull
	@Size(min = 4, max=20)
	private String oldPassword;
	
	@NotNull
	@Size(min = 4, max=20)
	private String newPassword;
	
	@NotNull
	@Size(min = 4, max=20)
	private String newPasswordConfirm;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}

	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

	@Override
	public String toString() {
		return "UserPasswordUpdate [oldPassword=" + oldPassword
				+ ", newPassword=" + newPassword + ", newPasswordConfirm="
				+ newPasswordConfirm + "]";
	}
}

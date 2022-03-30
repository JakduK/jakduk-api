package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.constraint.FieldMatch;
import com.jakduk.api.common.constraint.PasswordMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 이메일 기반 회원 비밀번호 변경 폼
 *
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     :
 */

@FieldMatch(first = "newPassword", second = "newPasswordConfirm", message = "{validation.msg.new.password.mismatch}")
public class UserPasswordForm {

	@Size(min = 4, max=20)
	@NotEmpty
	@PasswordMatch
	private String password;

	@Size(min = 4, max=20)
	@NotEmpty
	private String newPassword;

	@Size(min = 4, max=20)
	@NotEmpty
	private String newPasswordConfirm;

	public String getPassword() {
		return password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}
}

package com.jakduk.api.restcontroller.user.vo;

import com.jakduk.core.common.constraints.FieldMatch;
import com.jakduk.core.common.constraints.PasswordMatch;
import com.jakduk.core.exception.FormValidationErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     :
 */

@Getter
@ApiModel(description = "이메일 기반 회원 비밀번호 변경 폼")
@FieldMatch(first = "newPassword", second = "newPasswordConfirm", message = FormValidationErrorCode.NEW_PASSWORD_MISMATCH)
public class UserPasswordForm {

	@ApiModelProperty(required = true, example = "password01")
	@NotEmpty(message = FormValidationErrorCode.PASSWORD_NOT_EMPTY)
	@Size(min = 4, max=20, message = FormValidationErrorCode.PASSWORD_SIZE)
	@PasswordMatch
	private String password;

	@ApiModelProperty(required = true, example = "password02")
	@NotEmpty(message = FormValidationErrorCode.NEW_PASSWORD_NOT_EMPTY)
	@Size(min = 4, max=20, message = FormValidationErrorCode.NEW_PASSWORD_SIZE)
	private String newPassword;

	@ApiModelProperty(required = true, example = "password02")
	@NotEmpty(message = FormValidationErrorCode.NEW_PASSWORD_CONFIRM_NOT_EMPTY)
	@Size(min = 4, max=20, message = FormValidationErrorCode.NEW_PASSWORD_CONFIRM_SIZE)
	private String newPasswordConfirm;
}

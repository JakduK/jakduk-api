package com.jakduk.restcontroller.user.vo;

import com.jakduk.common.constraints.ExistEmailOnEdit;
import com.jakduk.common.constraints.ExistUsernameOnEdit;
import com.jakduk.exception.FormValidationErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     : SNS 기반 회원 가입 폼.
 */

@Data
@ApiModel(value = "회원 정보 편집 폼")
public class UserProfileOnEditForm {

	@ApiModelProperty(required = true, example = "example@jakduk.com")
	@NotEmpty(message = FormValidationErrorCode.EMAIL_NOT_EMPTY)
	@Size(min = 6, max=30, message = FormValidationErrorCode.EMAIL_SIZE)
	@Email(message = FormValidationErrorCode.EMAIL_NOT_FORMAT)
	@ExistEmailOnEdit
	private String email;

	@ApiModelProperty(required = true, example = "JakdukUser")
	@NotEmpty(message = FormValidationErrorCode.USERNAME_NOT_EMPTY)
	@Size(min = 2, max=20, message = FormValidationErrorCode.USERNAME_SIZE)
	@ExistUsernameOnEdit
	private String username;

	@ApiModelProperty(example = "안녕하세요.", value = "자기 소개")
	private String about;

	@ApiModelProperty(value = "축구단(FootballClub) ID")
	private String footballClub;
}

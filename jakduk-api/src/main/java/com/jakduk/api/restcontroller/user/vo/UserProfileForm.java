package com.jakduk.api.restcontroller.user.vo;

import com.jakduk.core.common.constraints.ExistEmailCompatibility;
import com.jakduk.core.common.constraints.ExistUsernameCompatibility;
import com.jakduk.core.exception.FormValidationErrorCode;
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
@ApiModel(value = "SNS 기반 회원 가입 폼")
@ExistEmailCompatibility(userId = "id", email = "email")
@ExistUsernameCompatibility(userId = "id", username = "username")
public class UserProfileForm {

	// 하위 호환성 유지를 위함. https://github.com/Pyohwan/JakduK/issues/53
	@ApiModelProperty(value = "회원 ID(필요할 때만)")
	private String id;

	@ApiModelProperty(required = true, example = "example@jakduk.com")
	@NotEmpty(message = FormValidationErrorCode.EMAIL_NOT_EMPTY)
	@Size(min = 6, max=30, message = FormValidationErrorCode.EMAIL_SIZE)
	@Email(message = FormValidationErrorCode.EMAIL_NOT_FORMAT)
	private String email;

	@ApiModelProperty(required = true, example = "JakdukUser")
	@NotEmpty(message = FormValidationErrorCode.USERNAME_NOT_EMPTY)
	@Size(min = 2, max=20, message = FormValidationErrorCode.USERNAME_SIZE)
	private String username;

	@ApiModelProperty(example = "안녕하세요.", value = "자기 소개")
	private String about;

	@ApiModelProperty(value = "축구단(FootballClub) ID")
	private String footballClub;
}

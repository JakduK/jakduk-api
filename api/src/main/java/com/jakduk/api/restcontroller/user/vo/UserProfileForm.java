package com.jakduk.api.restcontroller.user.vo;

import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistUsername;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     : SNS 기반 회원 가입 폼.
 */

@ApiModel(description = "SNS 기반 회원 가입 폼")
@Getter
public class UserProfileForm {

	@ApiModelProperty(required = true, example = "example@jakduk.com")
	@Size(min = 6, max=30)
	@NotEmpty
	@Email
	@ExistEmail
	private String email;

	@ApiModelProperty(required = true, example = "JakdukUser")
	@Size(min = 2, max=20)
	@NotEmpty
	@ExistUsername
	private String username;

	@ApiModelProperty(value = "축구단(FootballClub) ID")
	private String footballClub;

	@ApiModelProperty(example = "안녕하세요.", value = "자기 소개")
	private String about;
}

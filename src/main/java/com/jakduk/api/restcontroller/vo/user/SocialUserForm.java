package com.jakduk.api.restcontroller.vo.user;

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
public class SocialUserForm {

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

	@ApiModelProperty(example = "54e1d2c68bf86df3fe819874", value = "축구단(FootballClub) ID")
	private String footballClub;

	@ApiModelProperty(example = "안녕하세요.", value = "자기 소개")
	private String about;

	@ApiModelProperty(example = "58ad9b35a0c73a045d45979a", value = "UserPicture의 ID")
	private String userPictureId;

	@ApiModelProperty(example = "https://img1.daumcdn.net/thumb/R158x158/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2FSjuNejHmr8o0&t=1488000722876", value = "SNS계정의 회원 큰 사진")
	private String externalLargePictureUrl;

}

package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.constraint.ExistEmailOnEdit;
import com.jakduk.api.common.constraint.ExistUsernameOnEdit;
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
 * @desc     : 회원 편집 폼
 */

@ApiModel(description = "회원 정보 편집 폼")
@Getter
public class UserProfileEditForm {

	@ApiModelProperty(required = true, example = "test16@test.com")
	@Size(min = 6, max=30)
	@NotEmpty
	@Email
	@ExistEmailOnEdit
	private String email;

	@ApiModelProperty(required = true, example = "test16")
	@Size(min = 2, max=20)
	@NotEmpty
	@ExistUsernameOnEdit
	private String username;

	@ApiModelProperty(example = "안녕하세요.", value = "자기 소개")
	private String about;

	@ApiModelProperty(value = "축구단(FootballClub) ID")
	private String footballClub;

	@ApiModelProperty(example = "58ad9b35a0c73a045d45979a", value = "UserPicture의 ID")
	private String userPictureId;
}

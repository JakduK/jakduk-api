package com.jakduk.restcontroller.user.vo;

import com.jakduk.common.CommonConst;
import com.jakduk.model.embedded.LocalName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     : 회원 정보 열람.
 */

@ApiModel(value = "회원 프로필 응답 객체")
@Setter
@Getter
public class UserProfileResponse {

	@ApiModelProperty(value = "이메일")
	private String email;

	@ApiModelProperty(value = "별명")
	private String username;

	@ApiModelProperty(value = "소개")
	private String about;

	@ApiModelProperty(value = "provider ID")
	private CommonConst.ACCOUNT_TYPE providerId;

	@ApiModelProperty(value = "지지 축구단")
	private LocalName footballClubName;
}

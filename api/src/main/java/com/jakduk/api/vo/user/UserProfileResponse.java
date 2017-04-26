package com.jakduk.api.vo.user;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.embedded.UserPictureInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     : 회원 정보 열람.
 */

@ApiModel(description = "회원 프로필 응답 객체")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserProfileResponse {

	@ApiModelProperty(value = "이메일")
	private String email;

	@ApiModelProperty(value = "별명")
	private String username;

	@ApiModelProperty(value = "소개")
	private String about;

	@ApiModelProperty(value = "provider ID")
	private CoreConst.ACCOUNT_TYPE providerId;

	@ApiModelProperty(value = "지지 축구단 객체")
	@Setter
	private LocalName footballClubName;

	@ApiModelProperty(value = "회원 사진 객체")
	@Setter
	private UserPictureInfo picture;
}

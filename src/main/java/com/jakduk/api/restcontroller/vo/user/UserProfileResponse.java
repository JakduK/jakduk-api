package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.model.embedded.UserPictureInfo;
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

	@ApiModelProperty(example = "example@jakduk.com", value = "이메일")
	private String email;

	@ApiModelProperty(example = "작두왕", value = "별명")
	private String username;

	@ApiModelProperty(example = "나는 작두왕이다.", value = "소개")
	private String about;

	@ApiModelProperty(value = "provider ID")
	private Constants.ACCOUNT_TYPE providerId;

	@ApiModelProperty(value = "지지 축구단 객체")
	@Setter
	private LocalName footballClubName;

	@ApiModelProperty(value = "회원 사진 객체")
	@Setter
	private UserPictureInfo picture;

	@ApiModelProperty(example="true", value = "임시로 발급한 이메일인지 여부")
	private Boolean temporaryEmail;

}

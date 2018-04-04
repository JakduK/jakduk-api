package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistUsername;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * SNS 기반 회원 가입 폼
 *
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     : SNS 기반 회원 가입 폼.
 */

@Getter
@Builder
public class SocialUserForm {

	@Size(min = 6, max=30)
	@NotEmpty
	@Email
	@ExistEmail
	private String email;

	@Size(min = 2, max=20)
	@NotEmpty
	@ExistUsername
	private String username;

	private String footballClub; // 축구단(FootballClub) ID
	private String about; // 자기 소개
	private String userPictureId; // UserPicture의 ID
	private String externalLargePictureUrl; // SNS계젱에서 가져온 회원 큰 사진

}

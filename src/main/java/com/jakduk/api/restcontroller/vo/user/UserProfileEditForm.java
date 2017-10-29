package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistUsername;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 회원 정보 편집 폼
 *
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     : 회원 편집 폼
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserProfileEditForm {

	@Size(min = 6, max=30)
	@NotEmpty
	@Email
	@ExistEmail
	private String email;

	@Size(min = 2, max=20)
	@NotEmpty
	@ExistUsername
	private String username;

	private String about; // 자기 소개
	private String footballClub; // 축구단(FootballClub) ID
	private String userPictureId; // UserPicture의 ID
}

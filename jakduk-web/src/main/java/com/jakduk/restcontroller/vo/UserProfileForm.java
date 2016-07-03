package com.jakduk.restcontroller.vo;

import com.jakduk.common.CommonConst;
import com.jakduk.common.constraints.ExistEmail;
import com.jakduk.common.constraints.ExistEmailCompatibility;
import com.jakduk.common.constraints.ExistUsername;
import com.jakduk.common.constraints.FieldMatch;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     : 회원 등록시 사용.
 */

@Data
@ExistEmailCompatibility(userId = "id", email = "email")
//@FieldMatch(first = "email", second = "username", message = "PASSWORD_MISMATCH")
public class UserProfileForm {

	// 하위 호환성 유지를 위함. https://github.com/Pyohwan/JakduK/issues/53
	private String id;

	//@ExistEmail
	@NotEmpty(message = "EMAIL_NOT_EMPTY")
	@Size(min = 6, max=30, message = "EMAIL_SIZE")
	@Email(message = "EMAIL_NOT_FORMAT")
	private String email;

	@ExistUsername
	@NotEmpty(message = "USERNAME_NOT_EMPTY")
	@Size(min = 2, max=20, message = "USERNAME_SIZE")
	private String username;
	
	private String about;
	
	private String footballClub;
}

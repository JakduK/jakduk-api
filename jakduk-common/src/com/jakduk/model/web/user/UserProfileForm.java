package com.jakduk.model.web.user;

import com.jakduk.common.CommonConst;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     : 회원 등록시 사용.
 */

@Data
public class UserProfileForm {

	@Id
	private String id;				// 하위 호환성 유지를 위함. https://github.com/Pyohwan/JakduK/issues/53

	@NotNull
	@Size(min = 6, max=30)
	private String email;
	
	@NotNull
	@Size(min = 2, max=20)
	private String username;
	
	private String about;
	
	private String footballClub;

	private CommonConst.VALIDATION_TYPE emailStatus = CommonConst.VALIDATION_TYPE.NONE;		// 이메일 겹침 검사 상태 값

	private CommonConst.VALIDATION_TYPE usernameStatus = CommonConst.VALIDATION_TYPE.NONE;	// 별명 겹침 검사 상태 값
}

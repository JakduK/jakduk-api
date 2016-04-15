package com.jakduk.model.web;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 2.
 * @desc     :
 */

@Data
public class SocialUserForm {

	@NotNull
	@Size(min = 6, max=30)
	private String email;

	@NotNull
	@Size(min = 2, max=20)
	private String username;
	
	private String about;
	
	private String footballClub;

	/**
	 * 이메일 겹침 검사 상태 값
	 */
	private String emailStatus = "none";
	
	private String usernameStatus = "none";

}

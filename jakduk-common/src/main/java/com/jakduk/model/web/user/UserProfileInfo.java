package com.jakduk.model.web.user;

import com.jakduk.model.embedded.LocalName;
import lombok.Data;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     : 회원 정보 열람.
 */

@Data
public class UserProfileInfo {

	private String email;
	
	private String username;
	
	private String about;
	
	private LocalName footballClubName;
}

package com.jakduk.core.model.simple;

import com.jakduk.core.common.CommonConst;
import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 6.
 * @desc     :
 */

@Data
public class UserOnAuthentication {
	
	private String id;
	
	private String email;
	
	private String username;

	private String password;

	private CommonConst.ACCOUNT_TYPE providerId;

	private String providerUserId;
	
	private List<Integer> roles;

}

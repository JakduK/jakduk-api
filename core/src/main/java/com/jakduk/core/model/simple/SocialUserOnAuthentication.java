package com.jakduk.core.model.simple;

import com.jakduk.core.common.CoreConst;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 12.
 * @desc     :
 */

@Data
public class SocialUserOnAuthentication {
	
	@Id
	private String id;

	private String email;
	
	private String username;

	private CoreConst.ACCOUNT_TYPE providerId;

	private String providerUserId;
	
	private List<Integer> roles;

}

package com.jakduk.api.configuration.authentication.user;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.User;
import lombok.*;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonPrincipal {

	private String id;

	private String email;
	
	private String username;
	
	private CoreConst.ACCOUNT_TYPE providerId;

	public CommonPrincipal(JakdukUserDetails jakdukUserDetails) {
		this.id = jakdukUserDetails.getId();
		this.email = jakdukUserDetails.getUsername();
		this.username = jakdukUserDetails.getNickname();
		this.providerId = jakdukUserDetails.getProviderId();
	}

	public CommonPrincipal(SocialUserDetails socialUserDetails) {
		this.id = socialUserDetails.getId();
		this.email = socialUserDetails.getUserId();
		this.username = socialUserDetails.getUsername();
		this.providerId = socialUserDetails.getProviderId();
	}

	public CommonPrincipal(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.providerId = user.getProviderId();
	}
}

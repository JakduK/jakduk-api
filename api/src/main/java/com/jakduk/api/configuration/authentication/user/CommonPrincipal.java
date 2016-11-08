package com.jakduk.api.configuration.authentication.user;

import com.jakduk.core.common.CommonConst;
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
	
	private CommonConst.ACCOUNT_TYPE providerId;

	public CommonPrincipal(JakdukUserDetail jakdukUserDetail) {
		this.id = jakdukUserDetail.getId();
		this.email = jakdukUserDetail.getUsername();
		this.username = jakdukUserDetail.getNickname();
		this.providerId = jakdukUserDetail.getProviderId();
	}

	public CommonPrincipal(SocialUserDetail socialUserDetail) {
		this.id = socialUserDetail.getId();
		this.email = socialUserDetail.getUserId();
		this.username = socialUserDetail.getUsername();
		this.providerId = socialUserDetail.getProviderId();
	}

	public CommonPrincipal(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.providerId = user.getProviderId();
	}
}

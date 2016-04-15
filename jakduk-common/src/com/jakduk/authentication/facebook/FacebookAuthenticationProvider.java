package com.jakduk.authentication.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import com.jakduk.authentication.common.OAuthDetailService;
import com.jakduk.authentication.common.OAuthPrincipal;
import com.jakduk.common.CommonConst;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 27.
 * @desc     :
 */
public class FacebookAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private OAuthDetailService oauthDetailService;
	
	private FacebookService facebookService;

	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Only FacebookAuthenticationProvider is supported");

		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

		if (username.equals(CommonConst.ACCOUNT_TYPE.FACEBOOK)) {
			FacebookUser user = facebookService.findUser();

//			logger.debug("phjang=" + facebookUser);
			OAuthPrincipal principal = (OAuthPrincipal) oauthDetailService.loadUser(user.getId(), user.getName(), CommonConst.ACCOUNT_TYPE.FACEBOOK);
			
			UsernamePasswordAuthenticationToken token = 
					new UsernamePasswordAuthenticationToken(principal, authentication.getCredentials(), principal.getAuthorities());

			token.setDetails(oauthDetailService.getUserDetails(user));
			
			return token;			
		} else {
			throw new BadCredentialsException("fail to authenticate Facebook");
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}



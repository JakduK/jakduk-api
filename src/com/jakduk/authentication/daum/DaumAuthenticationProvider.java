package com.jakduk.authentication.daum;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.jakduk.authentication.common.OAuthDetailService;
import com.jakduk.authentication.common.OAuthPrincipal;
import com.jakduk.common.CommonConst;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 11.
 * @desc     :
 */
public class DaumAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private OAuthDetailService oauthDetailService;
	
	private DaumService daumService;
	
	private Logger logger = Logger.getLogger(this.getClass());

	public void setDaumService(DaumService daumService) {
		this.daumService = daumService;
	}

	@Override	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		
		if (username.equals(CommonConst.OAUTH_TYPE_DAUM)) {
			
			DaumUser user = daumService.findUser();
			
			logger.debug("phjang user=" + user);

			OAuthPrincipal principal = (OAuthPrincipal) oauthDetailService.loadUser(user.getUserid(), user.getNickname(), CommonConst.OAUTH_TYPE_DAUM);
			
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, authentication.getCredentials(), principal.getAuthorities());

			token.setDetails(oauthDetailService.getUserDetails(user));
			
			return token;
		} else {
			throw new BadCredentialsException("fail to authenticate Daum");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}

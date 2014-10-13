package com.jakduk.authentication.daum;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.jakduk.authentication.facebook.FacebookDetails;
import com.jakduk.authentication.facebook.FacebookUser;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 11.
 * @desc     :
 */
public class DaumAuthenticationProvider implements AuthenticationProvider {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private DaumService daumService;

	public void setDaumService(DaumService daumService) {
		this.daumService = daumService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		
		daumService.findUser();
		
		if (username.equals("daum")) {
			daumService.findUser();

//			FacebookDetails facebookUserDetails = (FacebookDetails) facebookUserDetailService.loadUser(facebookUser.getId(), facebookUser.getName());
			
//			UsernamePasswordAuthenticationToken token = 	new UsernamePasswordAuthenticationToken(facebookUserDetails, authentication.getCredentials(), facebookUserDetails.getAuthorities());

//			token.setDetails(getUserDetails(facebookUser));
			
//			return token;
			return null;
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}

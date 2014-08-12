package com.jakduk.authentication.facebook;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 27.
 * @desc     :
 */
public class FacebookAuthenticationProvider implements AuthenticationProvider {
	
	private Logger logger = Logger.getLogger(this.getClass());
    
    @Autowired
    private FacebookUserDetailService facebookUserDetailService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(FacebookAuthenticationToken.class, authentication, "Only FacebookAuthenticationProvider is supported");
		
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		
//		FacebookUser facebookUser = facebookService.findUser();
		
		logger.debug("phjang=" + authentication);
//		logger.debug("phjang=" + facebookUser);
		FacebookUserDetails fUser = (FacebookUserDetails) facebookUserDetailService.loadUserByUsername(username);
		FacebookAuthenticationToken token = new FacebookAuthenticationToken(fUser, authentication.getCredentials(), fUser.getAuthorities());
		token.setDetails(fUser);
		return token;
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return (FacebookAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

package com.jakduk.authentication.facebook;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import com.jakduk.authentication.common.UserDetails;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 27.
 * @desc     :
 */
public class FacebookAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private FacebookDetailService facebookUserDetailService;
	
	private FacebookService facebookService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Only FacebookAuthenticationProvider is supported");

//		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		
		FacebookUser facebookUser = facebookService.findUser();

		logger.debug("phjang=" + authentication);
		logger.debug("phjang=" + facebookUser);
//		FacebookDetails facebookUserDetails = (FacebookDetails) facebookUserDetailService.loadUserByUsername(facebookUser.getId());
		FacebookDetails facebookUserDetails = (FacebookDetails) facebookUserDetailService.loadUser(facebookUser.getId(), facebookUser.getUsername());
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(facebookUserDetails, authentication.getCredentials(), facebookUserDetails.getAuthorities());

		token.setDetails(getUserDetails(facebookUser));
		return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
	
	public UserDetails getUserDetails(FacebookUser facebookUser) {
		UserDetails userDetails = new UserDetails();
		
		if (facebookUser.getEmail() != null) {
			userDetails.setEmail(facebookUser.getEmail());
		}
		
		if (facebookUser.getGender() != null) {
			userDetails.setGender(facebookUser.getGender());
		}
		
		if (facebookUser.getBirthday() != null) {
			userDetails.setBirthday(facebookUser.getBirthday());
		}
		
		if (facebookUser.getLink() != null) {
			userDetails.setLink(facebookUser.getLink());
		}
		
		if (facebookUser.getLocale() != null) {
			userDetails.setLocale(facebookUser.getLocale());
		}
		
		if (facebookUser.getBio() != null) {
			userDetails.setBio(facebookUser.getBio());
		}
		
		return userDetails;
	}
}



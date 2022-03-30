package com.jakduk.api.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Created by pyohwanjang on 2017. 4. 30..
 */

@Component
public class SnsAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String email = authentication.getPrincipal().toString();

		UserDetails userDetails = userDetailsService.loadUserByUsername(email);

		return new SnsAuthenticationToken(userDetails, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SnsAuthenticationToken.class.isAssignableFrom(authentication);
	}
}

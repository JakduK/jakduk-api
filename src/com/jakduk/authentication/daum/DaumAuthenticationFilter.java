package com.jakduk.authentication.daum;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 11.
 * @desc     :
 */
public class DaumAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	protected DaumAuthenticationFilter(
			RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
		// TODO Auto-generated constructor stub
	}

	public DaumAuthenticationFilter() {
		super("/daum_login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("daum", null);
		SecurityContextHolder.getContext().setAuthentication(authRequest);
		
		logger.debug("phjang daum=");
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}

}

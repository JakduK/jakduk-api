package com.jakduk.authentication.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.jakduk.common.CommonConst;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 11.
 * @desc     :
 */
public class OAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private Logger logger = Logger.getLogger(this.getClass());
	
	protected OAuthProcessingFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,	FilterChain chain) throws IOException, ServletException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		
		if (!authentication.isAuthenticated() && (!httpRequest.getServletPath().equals("/oauth/daum/callback") || 
				!httpRequest.getServletPath().equals("/oauth/write"))) {
			SecurityContextHolder.getContext().setAuthentication(null);
			
			if (logger.isInfoEnabled()) {
				logger.info("oauth was cancled. Authentication object was deleted.");
			}
		}
		
		super.doFilter(req, res, chain);
	}

	public OAuthProcessingFilter() {
		super("/oauth/daum/callback");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

		String type = request.getParameter("type");
		String error = request.getParameter("error");
		
		if (error != null) {
			throw new BadCredentialsException(error);
		}
		
		if (type!= null && (type.equals(CommonConst.OAUTH_TYPE_DAUM) || type.equals(CommonConst.OAUTH_TYPE_FACEBOOK))) {
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(type, null);
			SecurityContextHolder.getContext().setAuthentication(authRequest);

			return this.getAuthenticationManager().authenticate(authRequest);
		} else {
			throw new AuthenticationCredentialsNotFoundException("not found OAuth account");
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		super.successfulAuthentication(request, response, chain, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
					throws IOException, ServletException {
		if (failed instanceof AccessTokenRequiredException
				|| failed instanceof AccessTokenRequiredException) {
			// Need to force a redirect via the OAuth client filter, so rethrow
			// here
			throw failed;
		} else {
			// If the exception is not a Spring Security exception this will
			// result in a default error page
			super.unsuccessfulAuthentication(request, response, failed);
		}
	} 
}

package com.jakduk.authentication.common;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.jakduk.common.CommonConst;
import com.jakduk.service.CommonService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 13.
 * @desc     :
 */

@Slf4j
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	CommonService commonService;

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String loginRedirect = request.getParameter("loginRedirect");
		
		if (authentication.getPrincipal() instanceof OAuthPrincipal) {
			OAuthPrincipal principal = (OAuthPrincipal) authentication.getPrincipal();


			/*
			if (addInfoStatus.equals(CommonConst.OAUTH_ADDITIONAL_INFO_STATUS_BLANK)) {
				if (log.isDebugEnabled()) {
					log.debug("Didn't input your additional infomation. Redrict input form.");
				}
				
				String targetUrl = "/oauth/write";
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
				return;
			}
			*/
		}
		
		clearAuthenticationAttributes(request);
		
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			
			if (log.isDebugEnabled()) {
				log.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
			}
			
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
			return;
		}
		
		if (loginRedirect != null) {
			if (commonService.isRedirectUrl(loginRedirect)) {
				if (log.isDebugEnabled()) {
					log.debug("Redirecting to this Url: " + loginRedirect);
				}
				getRedirectStrategy().sendRedirect(request, response, loginRedirect);
				return;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Don't redirect to this Url" + loginRedirect);
				}
			}
		}
		
		if (savedRequest == null) {
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}        
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

}

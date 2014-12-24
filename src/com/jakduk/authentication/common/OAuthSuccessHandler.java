package com.jakduk.authentication.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.jakduk.common.CommonConst;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 13.
 * @desc     :
 */
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String loginRedirect = request.getParameter("loginRedirect");
		
		if (authentication.getPrincipal() instanceof OAuthPrincipal) {
			OAuthPrincipal principal = (OAuthPrincipal) authentication.getPrincipal();
			String addInfoStatus = principal.getAddInfoStatus();	
			
			if (addInfoStatus.equals(CommonConst.OAUTH_ADDITIONAL_INFO_STATUS_BLANK)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Didn't input your additional infomation. Redrict input form.");
				}
				
				String targetUrl = "/oauth/write";
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
				return;
			} 
		}
		
		clearAuthenticationAttributes(request);
		
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			
			if (logger.isDebugEnabled()) {
				logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
			}
			
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
			return;
		}
		
		if (loginRedirect != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Redirecting to this Url: " + loginRedirect);
			}
			
			getRedirectStrategy().sendRedirect(request, response, loginRedirect);
			return;
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

package com.jakduk.authentication.jakduk;

import com.jakduk.common.CommonConst;
import com.jakduk.repository.user.UserRepository;
import com.jakduk.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */

@Slf4j
@Component
public class JakdukSuccessHandler extends SimpleUrlAuthenticationSuccessHandler	 {

	@Autowired
	private CommonService commonService;

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		doCookie(request, response, authentication);

		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String loginRedirect = request.getParameter("loginRedirect");
		
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
			if (commonService.isRedirectUrl(URLDecoder.decode(loginRedirect, "UTF-8"))) {
				if (log.isDebugEnabled()) {
					log.debug("Redirecting to this Url: " + URLDecoder.decode(loginRedirect, "UTF-8"));
				}
				getRedirectStrategy().sendRedirect(request, response, URLDecoder.decode(loginRedirect, "UTF-8"));
				return;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Don't redirect to this Url" + URLDecoder.decode(loginRedirect, "UTF-8"));
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
	
	private void doCookie(HttpServletRequest request, HttpServletResponse response, 	Authentication authentication) {
		String remember = request.getParameter("remember");
		String path = String.format("%s/", request.getContextPath());
		
		if (remember != null && remember.equals("on")) {
			if (authentication.getPrincipal() instanceof JakdukPrincipal) {
				JakdukPrincipal authUser = (JakdukPrincipal) authentication.getPrincipal();
				String username = authUser.getUsername();
				
				commonService.setCookie(response, CommonConst.COOKIE_EMAIL, username, path);
				
				commonService.setCookie(response, CommonConst.COOKIE_REMEMBER, "1", path);
			}
		} else {
			commonService.releaseCookie(response, CommonConst.COOKIE_EMAIL, path);
			
			commonService.releaseCookie(response, CommonConst.COOKIE_REMEMBER, path);
		}
	}

}

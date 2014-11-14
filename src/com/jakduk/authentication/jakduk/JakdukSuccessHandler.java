package com.jakduk.authentication.jakduk;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.jakduk.common.CommonConst;
import com.jakduk.repository.UserRepository;
import com.jakduk.service.CommonService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */
public class JakdukSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler 
	implements AuthenticationSuccessHandler {
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
			Authentication authentication) throws IOException, ServletException {
		
		String remember = request.getParameter("remember");
		String path = String.format("%s/", request.getContextPath());
		
		if (remember != null && remember.equals("on")) {
			if (authentication.getPrincipal() instanceof JakdukPrincipal) {
				JakdukPrincipal authUser = (JakdukPrincipal) authentication.getPrincipal();
				String email = authUser.getEmail();
				
				commonService.setCookie(response, CommonConst.COOKIE_EMAIL, email, path);
				
				commonService.setCookie(response, CommonConst.COOKIE_REMEMBER, "1", path);
			}
		} else {
			commonService.releaseCookie(response, CommonConst.COOKIE_EMAIL, path);
			
			commonService.releaseCookie(response, CommonConst.COOKIE_REMEMBER, path);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}

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

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */
public class JakdukSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler 
	implements AuthenticationSuccessHandler {
	
	@Autowired
	UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
			Authentication authentication) throws IOException, ServletException {
		
		String remember = request.getParameter("remember");
		
		if (remember != null && remember.equals("on")) {
			if (authentication.getPrincipal() instanceof JakdukPrincipal) {
				JakdukPrincipal authUser = (JakdukPrincipal) authentication.getPrincipal();
				String email = authUser.getEmail();
				
				Cookie cookie1 = new Cookie(CommonConst.COOKIE_EMAIL, email);
				cookie1.setMaxAge(60 * 60 * 24); // a day
				response.addCookie(cookie1);
				
				Cookie cookie2 = new Cookie(CommonConst.COOKIE_REMEMBER, "1");
				cookie2.setMaxAge(60 * 60 * 24); // a day
				response.addCookie(cookie2);
			}
		} else {
			Cookie cookie1 = new Cookie(CommonConst.COOKIE_EMAIL, null);
			cookie1.setMaxAge(0); // remove
			response.addCookie(cookie1);
			
			Cookie cookie2 = new Cookie(CommonConst.COOKIE_REMEMBER, null);
			cookie2.setMaxAge(0); // a day
			response.addCookie(cookie2);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}

package com.jakduk.authority;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.jakduk.common.CommonConst;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		String remember = request.getParameter("remember");
		
		if (remember != null && remember.equals("on")) {
			String email = request.getParameter("j_username");

			Cookie cookie1 = new Cookie(CommonConst.COOKIE_EMAIL, email);
			cookie1.setMaxAge(60 * 60 * 24); // a day
			response.addCookie(cookie1);

			Cookie cookie2 = new Cookie(CommonConst.COOKIE_REMEMBER, "1");
			cookie2.setMaxAge(60 * 60 * 24); // a day
			response.addCookie(cookie2);
		} else {
			Cookie cookie1 = new Cookie(CommonConst.COOKIE_EMAIL, null);
			cookie1.setMaxAge(0); // remove
			response.addCookie(cookie1);
			
			Cookie cookie2 = new Cookie(CommonConst.COOKIE_REMEMBER, null);
			cookie2.setMaxAge(0); // a day
			response.addCookie(cookie2);
		}
		
		response.sendRedirect(request.getContextPath() + "/login?error=1");
		
		
	}

}

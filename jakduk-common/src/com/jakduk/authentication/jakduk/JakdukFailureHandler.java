package com.jakduk.authentication.jakduk;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.jakduk.common.CommonConst;
import com.jakduk.service.CommonService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */
public class JakdukFailureHandler implements AuthenticationFailureHandler {
	
	@Autowired
	CommonService commonService;
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		String remember = request.getParameter("remember");
		String loginRedirect = request.getParameter("loginRedirect");
		String path = String.format("%s/", request.getContextPath());
		String result = "failure";
		
		if (exception.getClass().isAssignableFrom(LockedException.class)) {
			result = "locked";
		}
		
		if (remember != null && remember.equals("on")) {
			String email = request.getParameter("j_username");
			
			commonService.setCookie(response, CommonConst.COOKIE_EMAIL, email, path);
			
			commonService.setCookie(response, CommonConst.COOKIE_REMEMBER, "1", path);
		} else {
			commonService.releaseCookie(response, CommonConst.COOKIE_EMAIL, path);
			
			commonService.releaseCookie(response, CommonConst.COOKIE_REMEMBER, path);
		}
		
		response.sendRedirect(request.getContextPath() + "/login?result=" + result + "&loginRedirect=" + loginRedirect);
		
	}

}

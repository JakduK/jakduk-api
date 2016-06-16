package com.jakduk.authentication.jakduk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.common.CommonConst;
import com.jakduk.common.RestError;
import com.jakduk.exception.FindUserButNotJakdukAccount;
import com.jakduk.exception.NotFoundJakdukAccountException;
import com.jakduk.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Objects;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */

@Slf4j
@Component
public class JakdukFailureHandler implements AuthenticationFailureHandler {

	@Resource
	LocaleResolver localeResolver;

	@Autowired
	CommonService commonService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
										HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {

		Locale locale = localeResolver.resolveLocale(request);

		String accept = request.getHeader("accept");
		String remember = request.getParameter("remember");
		String loginRedirect = request.getParameter("loginRedirect");
		String path = String.format("%s/", request.getContextPath());
		String result = "failure";
		String message = "";

		if (exception instanceof InternalAuthenticationServiceException) {
			AuthenticationException customException = (AuthenticationException)exception.getCause();

			if (Objects.nonNull(customException)) {
				if (customException instanceof FindUserButNotJakdukAccount) {
					result = "warning";
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.connect.with.sns",
							((FindUserButNotJakdukAccount) exception.getCause()).getProviderId());
				} else if (customException instanceof NotFoundJakdukAccountException)  {
					message = commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.not.found.jakduk.account");
				} else if (customException instanceof LockedException) {
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.login.failure.locked");
				}
			} else {
				message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.login.failure");
			}
		} else if (exception instanceof BadCredentialsException) {
			message = commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.wrong.password");
		} else {
			message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.login.failure");
		}

		if (Objects.nonNull(accept) && accept.contains("json")) {
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setCharacterEncoding("utf-8");

			RestError error = new RestError("AUTH_FAIL", message);

			String errorJson = new ObjectMapper().writeValueAsString(error);

			PrintWriter out = response.getWriter();
			out.print(errorJson);
			out.flush();
			out.close();
		} else {
			if (remember != null && remember.equals("on")) {
				String email = request.getParameter("j_username");

				commonService.setCookie(response, CommonConst.COOKIE_EMAIL, email, path);
				commonService.setCookie(response, CommonConst.COOKIE_REMEMBER, "1", path);
			} else {
				commonService.releaseCookie(response, CommonConst.COOKIE_EMAIL, path);
				commonService.releaseCookie(response, CommonConst.COOKIE_REMEMBER, path);
			}

			response.sendRedirect(request.getContextPath() + "/login?result=" + result + "&message=" + URLEncoder.encode(message, "UTF-8") + "&loginRedirect=" + loginRedirect);
		}
	}

}

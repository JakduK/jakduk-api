package com.jakduk.api.configuration.authentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.api.restcontroller.exceptionHandler.RestError;
import com.jakduk.core.exception.FindUserButNotJakdukAccount;
import com.jakduk.core.exception.NotFoundJakdukAccountException;
import com.jakduk.core.service.CommonService;
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
import java.util.Locale;
import java.util.Objects;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */

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

		String message = "";
		int status = HttpServletResponse.SC_BAD_REQUEST;

		if (exception instanceof InternalAuthenticationServiceException) {
			AuthenticationException customException = (AuthenticationException)exception.getCause();

			if (Objects.nonNull(customException)) {
				if (customException instanceof FindUserButNotJakdukAccount) {
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.connect.with.sns",
							((FindUserButNotJakdukAccount) exception.getCause()).getProviderId());
				} else if (customException instanceof NotFoundJakdukAccountException)  {
					message = commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.not.found.jakduk.account");
					status = HttpServletResponse.SC_NOT_FOUND;
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

		response.setContentType("application/json");
		response.setStatus(status);
		response.setCharacterEncoding("utf-8");

		RestError error = new RestError("-", message);

		String errorJson = new ObjectMapper().writeValueAsString(error);

		PrintWriter out = response.getWriter();
		out.print(errorJson);
		out.flush();
		out.close();

	}

}

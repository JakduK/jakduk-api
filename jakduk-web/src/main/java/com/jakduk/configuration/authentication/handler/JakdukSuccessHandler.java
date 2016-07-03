package com.jakduk.configuration.authentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.common.JakdukPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 11.
 * @desc     :
 */

@Component
public class JakdukSuccessHandler extends SimpleUrlAuthenticationSuccessHandler	 {

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication) throws ServletException, IOException {

		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			generateRestJson(response);
			clearAuthenticationAttributes(request);
			return;
		}
		String targetUrlParam = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl() ||
				(targetUrlParam != null &&
						StringUtils.hasText(request.getParameter(targetUrlParam)))) {
			requestCache.removeRequest(request, response);
			generateRestJson(response);
			clearAuthenticationAttributes(request);
			return;
		}

		generateRestJson(response);
		clearAuthenticationAttributes(request);
	}

	private void generateRestJson(HttpServletResponse response) throws IOException {
		JakdukPrincipal principal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		CommonPrincipal commonPrincipal = CommonPrincipal.builder()
				.id(principal.getId())
				.email(principal.getUsername())
				.username(principal.getNickname())
				.providerId(principal.getProviderId())
				.build();

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		String commonPrincipalJson = new ObjectMapper().writeValueAsString(commonPrincipal);

		PrintWriter out = response.getWriter();
		out.print(commonPrincipalJson);
		out.flush();
		out.close();
	}

}

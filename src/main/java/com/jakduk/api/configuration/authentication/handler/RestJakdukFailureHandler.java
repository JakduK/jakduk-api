package com.jakduk.api.configuration.authentication.handler;

import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.restcontroller.vo.RestErrorResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by pyohwanjang on 2017. 4. 30..
 */

public class RestJakdukFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setCharacterEncoding("utf-8");
        RestErrorResponse restErrorResponse;

        if (e.getClass().isAssignableFrom(BadCredentialsException.class)) {
            restErrorResponse = new RestErrorResponse(ServiceError.BAD_CREDENTIALS);
        } else {
            restErrorResponse = new RestErrorResponse(ServiceError.UNAUTHORIZED_ACCESS, e.getLocalizedMessage());
        }

        String errorJson = ObjectMapperUtils.getObjectMapper().writeValueAsString(restErrorResponse);

        PrintWriter out = httpServletResponse.getWriter();
        out.print(errorJson);
        out.flush();
        out.close();
    }
}

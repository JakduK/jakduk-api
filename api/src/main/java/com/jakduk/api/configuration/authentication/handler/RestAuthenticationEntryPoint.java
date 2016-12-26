package com.jakduk.api.configuration.authentication.handler;

import com.jakduk.api.restcontroller.exception.ApiRestErrorResponse;
import com.jakduk.core.common.util.ObjectMapperUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author pyohwan
 * 16. 6. 22 오전 12:43
 */

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    CommonService commonService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ServiceError serviceError = ServiceError.NEED_TO_LOGIN;
        ApiRestErrorResponse apiRestErrorResponse;

        if (authException.getClass().isAssignableFrom(BadCredentialsException.class)) {
            serviceError = ServiceError.BAD_CREDENTIALS;
            apiRestErrorResponse = new ApiRestErrorResponse(serviceError);
        } else {
            apiRestErrorResponse = new ApiRestErrorResponse(serviceError.getCode(), authException.getLocalizedMessage());
        }

        response.setContentType("application/json");
        response.setStatus(serviceError.getHttpStatus());
        response.setCharacterEncoding("utf-8");

        String errorJson = ObjectMapperUtils.getObjectMapper().writeValueAsString(apiRestErrorResponse);

        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
        out.close();
    }
}

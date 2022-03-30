package com.jakduk.api.configuration.security.handler;

import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.restcontroller.vo.RestErrorResponse;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author pyohwan
 * 16. 6. 22 오전 12:43
 */

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ServiceError serviceError = ServiceError.NEED_TO_LOGIN;

        if (authException.getClass().isAssignableFrom(BadCredentialsException.class))
            serviceError = ServiceError.BAD_CREDENTIALS;

        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setStatus(serviceError.getHttpStatus());

        RestErrorResponse restErrorResponse = new RestErrorResponse(serviceError);
        String errorJson = ObjectMapperUtils.writeValueAsString(restErrorResponse);

        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
        out.close();
    }
}

package com.jakduk.api.configuration.authentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.api.restcontroller.exception.ApiRestErrorResponse;
import com.jakduk.core.exception.ServiceExceptionCode;
import com.jakduk.core.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
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

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");

        ApiRestErrorResponse error = new ApiRestErrorResponse(ServiceExceptionCode.NEED_TO_LOGIN);
        String errorJson = new ObjectMapper().writeValueAsString(error);

        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
        out.close();
    }
}

package com.jakduk.configuration.authentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.restcontroller.exceptionHandler.RestError;
import com.jakduk.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * @author pyohwan
 * 16. 6. 22 오전 12:44
 */

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    CommonService commonService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("utf-8");

        RestError error = new RestError("-", commonService.getResourceBundleMessage("messages.common", "common.exception.access.denied"));

        String errorJson = new ObjectMapper().writeValueAsString(error);

        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
        out.close();
    }
}

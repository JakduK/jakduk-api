package com.jakduk.api.configuration.security.handler;

import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by pyohwanjang on 2017. 4. 30..
 */

public class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");

        String errorJson = ObjectMapperUtils.getObjectMapper().writeValueAsString(EmptyJsonResponse.newInstance());

        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
        out.close();
    }
}

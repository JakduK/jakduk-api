package com.jakduk.configuration.authentication.handler;

import com.jakduk.common.CommonConst;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by pyohwan on 16. 6. 23.
 */

@Component
public class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrintWriter out = response.getWriter();
        out.print(CommonConst.RESPONSE_VOID_OBJECT);
        out.flush();
        out.close();
    }
}

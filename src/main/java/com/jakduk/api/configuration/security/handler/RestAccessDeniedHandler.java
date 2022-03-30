package com.jakduk.api.configuration.security.handler;

import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.restcontroller.vo.RestErrorResponse;
import org.apache.commons.codec.CharEncoding;
import org.apache.http.entity.ContentType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author pyohwan
 * 16. 6. 22 오전 12:44
 */

public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(CharEncoding.UTF_8);

        RestErrorResponse restErrorResponse = new RestErrorResponse(ServiceError.UNAUTHORIZED_ACCESS);
        String errorJson = ObjectMapperUtils.getObjectMapper().writeValueAsString(restErrorResponse);

        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
        out.close();
    }
}

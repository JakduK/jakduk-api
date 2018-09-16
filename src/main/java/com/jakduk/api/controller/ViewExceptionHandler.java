package com.jakduk.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.restcontroller.vo.RestErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by pyohwan on 17. 2. 6.
 */

@ControllerAdvice("com.jakduk.api.controller")
public class ViewExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    @ExceptionHandler({ ServiceException.class })
    public ModelAndView handleServiceException(HttpServletRequest request, ServiceException exception) {

        ServiceError serviceError = exception.getServiceError();
        HttpStatus httpStatus = HttpStatus.valueOf(serviceError.getHttpStatus());

        RestErrorResponse restErrorResponse = new RestErrorResponse(serviceError, exception.getLocalizedMessage());

        String logMessage;

        try {
            logMessage = ObjectMapperUtils.writeValueAsString(restErrorResponse);
        } catch (JsonProcessingException e) {
            logMessage = "code : " + exception.getServiceError().getCode() + ", message : " + exception.getLocalizedMessage();
        }

        ModelAndView modelAndView = new ModelAndView();

        if (httpStatus.is4xxClientError()) {
            modelAndView.setViewName("error/4xx");
            log.warn(logMessage, exception);

        } else if (httpStatus.is5xxServerError()) {
            modelAndView.setViewName("error/" + httpStatus.toString());
            log.error(logMessage, exception);
        }

        Map<String, Object> map = getErrorAttributes(request, false);
        modelAndView.addAllObjects(map);
        modelAndView.setStatus(httpStatus);
        modelAndView.addObject("status", httpStatus.value());

        return modelAndView;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }
}

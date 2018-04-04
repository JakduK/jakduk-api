package com.jakduk.api.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.restcontroller.vo.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pyohwan
 * 16. 3. 4 오전 12:30
 */

@Slf4j
@ControllerAdvice("com.jakduk.api.restcontroller")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    /**
     * Hibernate validator Exception
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ObjectError> globalErrors = result.getGlobalErrors();
        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        Map<String, String> fields = new HashMap<>();

        globalErrors.forEach(
                error -> fields.put("global_" + error.getCode(), error.getDefaultMessage())
        );

        fieldErrors.forEach(
                error -> fields.put(error.getField() + "_" + error.getCode(), error.getDefaultMessage())
        );

        RestErrorResponse restErrorResponse = new RestErrorResponse(serviceError, fields);

        try {
            log.warn(ObjectMapperUtils.writeValueAsString(restErrorResponse), ex);
        } catch (JsonProcessingException ignore) {
            log.warn(ex.getLocalizedMessage(), ex);
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * RequestBody에서 request 값들을 객체화 실패했을때
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        RestErrorResponse restErrorResponse = new RestErrorResponse(serviceError);

        try {
            log.warn(ObjectMapperUtils.writeValueAsString(restErrorResponse), ex);
        } catch (JsonProcessingException ignore) {
            log.warn(ex.getLocalizedMessage(), ex);
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * 파라미터 검증 실패.
     *
     * multipart/form-data 에서 key가 file이 아닐때.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;
        RestErrorResponse restErrorResponse = new RestErrorResponse(serviceError);

        try {
            log.warn(ObjectMapperUtils.writeValueAsString(restErrorResponse), ex);
        } catch (JsonProcessingException ignore) {
            log.warn(ex.getLocalizedMessage(), ex);
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * RequestParam 에서 필드 검증 실패
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<Object> constrainViolationException(ConstraintViolationException ex) {

        Map<String, String> fields = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        violations.forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            fields.put(field, message);
        });

        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        RestErrorResponse restErrorResponse = new RestErrorResponse(serviceError, fields);

        try {
            log.warn(ObjectMapperUtils.writeValueAsString(restErrorResponse), ex);
        } catch (JsonProcessingException ignore) {
            log.warn(ex.getLocalizedMessage(), ex);
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * Spring Security 에서 발생하는 Exception
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<RestErrorResponse> authenticationException(AuthenticationException ex) {

        ServiceError serviceError = ServiceError.UNAUTHORIZED_ACCESS;

        if (ex.getCause().getClass().isAssignableFrom(ServiceException.class)) {
            serviceError = ((ServiceException)ex.getCause()).getServiceError();
        }

        RestErrorResponse restErrorResponse = new RestErrorResponse(
                serviceError.getCode(), ex.getLocalizedMessage(), serviceError.getHttpStatus()
        );

        try {
            log.warn(ObjectMapperUtils.writeValueAsString(restErrorResponse), ex);
        } catch (JsonProcessingException ignore) {
            log.warn(ex.getLocalizedMessage(), ex);
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * 접근 거부. 로그인 필요
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestErrorResponse> accessDeniedException(AccessDeniedException ex) {

        ServiceError serviceError = ServiceError.NEED_TO_LOGIN;

        RestErrorResponse restErrorResponse = new RestErrorResponse(
                serviceError.getCode(), ex.getLocalizedMessage(), serviceError.getHttpStatus()
        );

        try {
            log.warn(ObjectMapperUtils.writeValueAsString(restErrorResponse));
        } catch (JsonProcessingException ignore) {
            log.warn(ex.getLocalizedMessage());
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }


    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<RestErrorResponse> serviceException(ServiceException ex) {
        ServiceError serviceError = ex.getServiceError();
        HttpStatus httpStatus = HttpStatus.valueOf(serviceError.getHttpStatus());

        RestErrorResponse restErrorResponse = new RestErrorResponse(serviceError, ex.getLocalizedMessage());

        String logMessage;

        try {
            logMessage = ObjectMapperUtils.writeValueAsString(restErrorResponse);
        } catch (JsonProcessingException e) {
            logMessage = "code : " + ex.getServiceError().getCode() + ", message : " + ex.getLocalizedMessage();
        }

        if (serviceError.equals(ServiceError.ANONYMOUS)) {
//            log.info(logMessage);
        } else if (httpStatus.is4xxClientError()) {
            log.warn(logMessage, ex);
        } else if (httpStatus.is5xxServerError()) {
            log.error(logMessage, ex);
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<RestErrorResponse> runtimeException(RuntimeException ex, HttpServletRequest request) {
        RestErrorResponse restErrorResponse = new RestErrorResponse(ServiceError.INTERNAL_SERVER_ERROR);
        restErrorResponse.setDetail(getErrorAttributes(request, false));

        try {
            log.error(ObjectMapperUtils.writeValueAsString(restErrorResponse), ex);
        } catch (JsonProcessingException ignore) {
            log.error(ex.getLocalizedMessage(), ex);
        }

        return new ResponseEntity<>(restErrorResponse, HttpStatus.valueOf(restErrorResponse.getHttpStatus()));
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        return errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }
}

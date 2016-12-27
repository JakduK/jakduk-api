package com.jakduk.api.restcontroller.exception;

import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.ServiceError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
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
@ControllerAdvice(value = "com.jakduk.api.restcontroller")
public class ApiRestExceptionHandler extends ResponseEntityExceptionHandler {

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

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceError, fields);

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * RequestBody에서 request 값들을 객체화 실패했을때
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceError);

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * RequestParam 에서 필드 검증 실패
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<Object> constrainViolationException(ConstraintViolationException e) {

        Map<String, String> fields = new HashMap<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        violations.forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            fields.put(field, message);
        });

        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceError, fields);

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * Spring Security 에서 발생하는 Exception
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ApiRestErrorResponse> authenticationException(AuthenticationException e) {

        ServiceError serviceError = ServiceError.UNAUTHORIZED_ACCESS;

        if (e.getCause().getClass().isAssignableFrom(ServiceException.class)) {
            serviceError = ((ServiceException)e.getCause()).getServiceError();
        }

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(
                serviceError.getCode(), e.getLocalizedMessage(), serviceError.getHttpStatus()
        );

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<ApiRestErrorResponse> serviceException(ServiceException e) {
        ServiceError serviceError = e.getServiceError();
        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceError, e.getLocalizedMessage());

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<ApiRestErrorResponse> runtimeException(HttpServletRequest request) {
        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(ServiceError.INTERNAL_SERVER_ERROR);
        apiRestErrorResponse.setDetail(getErrorAttributes(request, false));

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(apiRestErrorResponse.getHttpStatus()));
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);

        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
}

package com.jakduk.api.restcontroller.exception;

import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.ServiceExceptionCode;
import com.jakduk.core.exception.UserFeelingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

    /**
     * Hibernate validator Exception
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ObjectError> globalErrors = result.getGlobalErrors();
        ServiceExceptionCode serviceExceptionCode = ServiceExceptionCode.FORM_VALIDATION_FAILED;

        Map<String, String> fields = new HashMap<>();

        globalErrors.forEach(
                error -> fields.put("global_" + error.getCode(), error.getDefaultMessage())
        );

        fieldErrors.forEach(
                error -> fields.put(error.getField() + "_" + error.getCode(), error.getDefaultMessage())
        );

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceExceptionCode, fields);

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceExceptionCode.getHttpStatus()));
    }

    /**
     * RequestBody에서 request 값들을 객체화 실패했을때
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServiceExceptionCode serviceExceptionCode = ServiceExceptionCode.FORM_VALIDATION_FAILED;

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceExceptionCode);

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceExceptionCode.getHttpStatus()));
    }

    /**
     * 쿼리 스트링 검증 실패
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServiceExceptionCode serviceExceptionCode = ServiceExceptionCode.FORM_VALIDATION_FAILED;

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceExceptionCode.getCode(), ex.getLocalizedMessage());

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceExceptionCode.getHttpStatus()));
    }

    /**
     * 파라미터 검증 실패.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServiceExceptionCode serviceExceptionCode = ServiceExceptionCode.FORM_VALIDATION_FAILED;

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceExceptionCode.getCode(),
                String.format(ex.getMessage(), ex.getRequestPartName()));

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceExceptionCode.getHttpStatus()));
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

        ServiceExceptionCode serviceExceptionCode = ServiceExceptionCode.FORM_VALIDATION_FAILED;

        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceExceptionCode, fields);

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceExceptionCode.getHttpStatus()));
    }

    @ExceptionHandler(UserFeelingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiRestErrorResponse userFeelingException(UserFeelingException e) {
        return new ApiRestErrorResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<ApiRestErrorResponse> serviceException(ServiceException ex) {
        ServiceExceptionCode serviceExceptionCode = ex.getServiceExceptionCode();
        ApiRestErrorResponse apiRestErrorResponse = new ApiRestErrorResponse(serviceExceptionCode);

        return new ResponseEntity<>(apiRestErrorResponse, HttpStatus.valueOf(serviceExceptionCode.getHttpStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiRestErrorResponse runtimeException(RuntimeException e) {
        return new ApiRestErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }
}

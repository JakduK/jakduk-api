package com.jakduk.restcontroller.exceptionHandler;

import com.jakduk.common.CommonConst;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.exception.SuccessButNoContentException;
import com.jakduk.exception.UserFeelingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 3. 4.
 */

@Slf4j
@ControllerAdvice(value = "com.jakduk.restcontroller")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // 에러는 아니지만 데이터가 없음.
    @ExceptionHandler(SuccessButNoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public RestError successButNoContentException(SuccessButNoContentException e) {
        RestError restError = new RestError(HttpStatus.NO_CONTENT.toString(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler(UserFeelingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError repositoryExistException(UserFeelingException e) {
        RestError restError = new RestError(e.getCode(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<RestError> serviceException(ServiceException ex) {

        ServiceError serviceError = ex.getServiceError();
        RestError restError = new RestError(serviceError);

        return new ResponseEntity<>(restError, serviceError.getHttpStatus());
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError runtimeException(RuntimeException e) {
        RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
        return  restError;
    }

    // Hibernate validator Exception
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ObjectError> globalErrors = result.getGlobalErrors();

        List<String> fields = new ArrayList<>();

        for (ObjectError objectError : globalErrors) {
            fields.add(objectError.getDefaultMessage());
        }

        for (FieldError fieldError : fieldErrors) {
            fields.add(fieldError.getDefaultMessage());
        }

        RestError restError = new RestError(ServiceError.FORM_VALIDATION_FAILED, fields);

        return new ResponseEntity<>(restError, ServiceError.FORM_VALIDATION_FAILED.getHttpStatus());
    }
}



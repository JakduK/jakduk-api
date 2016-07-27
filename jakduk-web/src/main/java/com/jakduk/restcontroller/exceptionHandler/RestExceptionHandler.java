package com.jakduk.restcontroller.exceptionHandler;

import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.exception.SuccessButNoContentException;
import com.jakduk.exception.UserFeelingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import java.util.List;

/**
 * Created by pyohwan on 16. 3. 4.
 */

@Slf4j
@ControllerAdvice(value = "com.jakduk.restcontroller")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Hibernate validator Exception
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
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

    /**
     * RequestBody에서 request 값들을 객체화 실패했을때
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        RestError restError = new RestError(ServiceError.FORM_VALIDATION_FAILED);

        return new ResponseEntity<>(restError, ServiceError.FORM_VALIDATION_FAILED.getHttpStatus());
    }

    // 에러는 아니지만 데이터가 없음.
    @ExceptionHandler(SuccessButNoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public RestError successButNoContentException(SuccessButNoContentException e) {
        return new RestError(HttpStatus.NO_CONTENT.toString(), e.getMessage());
    }

    @ExceptionHandler(UserFeelingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError repositoryExistException(UserFeelingException e) {
        return new RestError(e.getCode(), e.getMessage());
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
        return new RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
    }
}



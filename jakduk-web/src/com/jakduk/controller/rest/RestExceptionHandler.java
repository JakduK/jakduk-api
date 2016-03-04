package com.jakduk.controller.rest;

import com.jakduk.common.CommonError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pyohwan on 16. 3. 4.
 */

@RestController
@ControllerAdvice(value = "com.jakduk.controller")
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public CommonError runtimeException(RuntimeException e) {

        CommonError commonError = new CommonError();
        commonError.setMessage(e.getMessage());

        return  commonError;
    }
}


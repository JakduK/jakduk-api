package com.jakduk.restcontroller;

import com.jakduk.common.RestError;
import com.jakduk.exception.RepositoryExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Created by pyohwan on 16. 3. 4.
 */

@ControllerAdvice(value = "com.jakduk.restcontroller", annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestError illegalArgumentException(IllegalArgumentException e) {
        RestError restError = new RestError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestError noSuchElementException(NoSuchElementException e) {
        RestError restError = new RestError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler(RepositoryExistException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public RestError repositoryExistException(RepositoryExistException e) {
        RestError restError = new RestError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError runtimeException(RuntimeException e) {
        RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return  restError;
    }
}


package com.jakduk.restcontroller;

import com.jakduk.common.RestError;
import com.jakduk.exception.RepositoryExistException;
import com.jakduk.exception.UserFeelingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Created by pyohwan on 16. 3. 4.
 */

@ControllerAdvice(value = "com.jakduk.restcontroller", annotations = RestController.class)
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestError illegalArgumentException(IllegalArgumentException e) {
        RestError restError = new RestError(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestError noSuchElementException(NoSuchElementException e) {
        RestError restError = new RestError(HttpStatus.NOT_FOUND.toString(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler(UserFeelingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError repositoryExistException(UserFeelingException e) {
        RestError restError = new RestError(e.getCode(), e.getMessage());
        return  restError;
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError runtimeException(RuntimeException e) {
        RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
        return  restError;
    }
}


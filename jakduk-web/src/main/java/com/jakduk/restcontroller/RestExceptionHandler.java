package com.jakduk.restcontroller;

import com.jakduk.common.RestError;
import com.jakduk.exception.SuccessButNoContentException;
import com.jakduk.exception.UserFeelingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Created by pyohwan on 16. 3. 4.
 */

@Slf4j
@ControllerAdvice(value = "com.jakduk.restcontroller")
public class RestExceptionHandler {

    // 잘못된 파라미터.
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestError illegalArgumentException(IllegalArgumentException e) {
        RestError restError = new RestError(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return  restError;
    }

    // 필수 데이터를 찾을수 없음
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestError noSuchElementException(NoSuchElementException e) {
        RestError restError = new RestError(HttpStatus.NOT_FOUND.toString(), e.getMessage());
        return  restError;
    }

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

    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError runtimeException(RuntimeException e) {
        RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
        return  restError;
    }
}


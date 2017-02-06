package com.jakduk.api.controller;

import com.jakduk.core.exception.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

/**
 * Created by pyohwan on 17. 2. 6.
 */

@ControllerAdvice
public class ViewExceptionHandler {

    @ExceptionHandler({ ServiceException.class })
    public String databaseError(ServiceException exception) {
        return "error/404";
    }
}

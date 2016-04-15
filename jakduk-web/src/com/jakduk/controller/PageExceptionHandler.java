package com.jakduk.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

/**
 * Created by pyohwan on 16. 4. 16.
 */

@ControllerAdvice(value = "com.jakduk.controller")
public class PageExceptionHandler {

    @PageExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView illegalArgumentException(IllegalArgumentException e) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", e.getMessage());
        return  modelAndView;
    }

    @PageExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView noSuchElementException(NoSuchElementException e) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", e.getMessage());
        return  modelAndView;
    }

    @PageExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView mysqlDataTruncation(DataAccessException e) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", e.getCause().toString());
        return  modelAndView;
    }

    @PageExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView runtimeException(RuntimeException e) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", e.getMessage());
        return  modelAndView;
    }

    @PageExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", e.getMessage());
        return  modelAndView;
    }
}

package com.jakduk.exception;

import lombok.Getter;

import javax.xml.ws.Service;

/**
 * @author pyohwan
 * 16. 7. 1 오전 12:30
 */

@Getter
public class ServiceException extends RuntimeException {

    private ServiceError serviceError;

    public ServiceException(ServiceError serviceError) {
        this.serviceError = serviceError;
    }

    public ServiceException(ServiceError serviceError, String message) {
        super(message);
        this.serviceError = serviceError;
    }
}

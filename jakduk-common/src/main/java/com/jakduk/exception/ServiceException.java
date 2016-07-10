package com.jakduk.exception;

import lombok.Getter;

import javax.xml.ws.Service;

/**
 * Created by pyohwan on 16. 7. 1.
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

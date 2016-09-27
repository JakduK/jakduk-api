package com.jakduk.core.exception;

import lombok.Getter;

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

    public ServiceException(ServiceError serviceError, Throwable throwable) {
        super(throwable);
        this.serviceError = serviceError;
    }

    public ServiceException(ServiceError serviceError, String message, Throwable throwable) {
        super(message, throwable);
        this.serviceError = serviceError;
    }
}

package com.jakduk.api.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author pyohwan
 * 16. 7. 1 오전 12:30
 */

public class ServiceException extends RuntimeException {

    @Getter
    private ServiceError serviceError;

    public ServiceException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }

    public ServiceException(ServiceError serviceError, String message) {
        super(message);
        this.serviceError = serviceError;
    }

    public ServiceException(ServiceError serviceError, Throwable throwable) {
        super(serviceError.getMessage(), throwable);
        this.serviceError = serviceError;
    }

    public ServiceException(ServiceError serviceError, String message, Throwable throwable) {
        super(message, throwable);
        this.serviceError = serviceError;
    }
}

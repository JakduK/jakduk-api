package com.jakduk.api.exception;

/**
 * @author pyohwan
 * 16. 7. 1 오전 12:30
 */

public class ServiceException extends RuntimeException {

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

    public ServiceError getServiceError() {
        return serviceError;
    }
}

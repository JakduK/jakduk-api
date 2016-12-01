package com.jakduk.core.exception;

import lombok.Getter;

/**
 * @author pyohwan
 * 16. 7. 1 오전 12:30
 */

public class ServiceException extends RuntimeException {

    @Getter
    private ServiceExceptionCode serviceExceptionCode;

    public ServiceException(ServiceExceptionCode serviceExceptionCode) {
        super(serviceExceptionCode.getMessage());
        this.serviceExceptionCode = serviceExceptionCode;
    }

    public ServiceException(ServiceExceptionCode serviceExceptionCode, String message) {
        super(message);
        this.serviceExceptionCode = serviceExceptionCode;
    }

    public ServiceException(ServiceExceptionCode serviceExceptionCode, Throwable throwable) {
        super(serviceExceptionCode.getMessage(), throwable);
        this.serviceExceptionCode = serviceExceptionCode;
    }

    public ServiceException(ServiceExceptionCode serviceExceptionCode, String message, Throwable throwable) {
        super(message, throwable);
        this.serviceExceptionCode = serviceExceptionCode;
    }
}

package com.jakduk.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by pyohwan on 16. 7. 1.
 */

@Getter
public enum ServiceError {

    FORM_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "FORM_VALIDATION_FAILED", "common.exception.invalid.parameter"),
    NOT_REGISTER_WITH_SNS(HttpStatus.NOT_FOUND, "NOT_REGISTER_WITH_SNS", "common.exception.not.register.with.sns"),
    CANNOT_GET_SNS_PROFILE(HttpStatus.NOT_FOUND, "CANNOT_GET_SNS_PROFILE", "common.exception.cannot.get.sns.profile");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    private ServiceError(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}

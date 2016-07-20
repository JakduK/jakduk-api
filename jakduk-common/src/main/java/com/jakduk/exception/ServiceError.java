package com.jakduk.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author pyohwan
 * 16. 7. 1 오후 11:23
 */

@Getter
public enum ServiceError {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", "common.exception.invalid.parameter"),
    FORM_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "FORM_VALIDATION_FAILED", "common.exception.invalid.parameter"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "common.exception.no.such.element"),
    NOT_REGISTER_WITH_SNS(HttpStatus.NOT_FOUND, "NOT_REGISTER_WITH_SNS", "common.exception.not.register.with.sns"),
    CANNOT_GET_SNS_PROFILE(HttpStatus.NOT_FOUND, "CANNOT_GET_SNS_PROFILE", "common.exception.cannot.get.sns.profile"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "common.exception.post.not.found"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "common.exception.category.not.found"),

    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ACCESS", "common.exception.access.denied"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "common.exception.forbidden");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ServiceError(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}

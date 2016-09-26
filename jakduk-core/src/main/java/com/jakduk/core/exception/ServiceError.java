package com.jakduk.core.exception;

import com.jakduk.core.service.CommonService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author pyohwan
 * 16. 7. 1 오후 11:23
 */

@Getter
public enum ServiceError {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "common.exception.invalid.parameter"),
    FORM_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "common.exception.invalid.parameter"),
    EXPIRATION_TOKEN(HttpStatus.BAD_REQUEST, "common.exception.expiration.token"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "common.exception.no.such.element"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "common.exception.not.found.user"),
    NOT_REGISTER_WITH_SNS(HttpStatus.NOT_FOUND, "common.exception.not.register.with.sns"),
    CANNOT_GET_SNS_PROFILE(HttpStatus.NOT_FOUND, "common.exception.cannot.get.sns.profile"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "common.exception.post.not.found"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "common.exception.category.not.found"),
    ALREADY_ENABLE(HttpStatus.NOT_FOUND, "common.exception.already.enable"),
    ALREADY_DISABLE(HttpStatus.NOT_FOUND, "common.exception.already.disable"),

    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "common.exception.access.denied"),
    NOT_FOUND_JAKDUK_ACCOUNT(HttpStatus.UNAUTHORIZED, "common.exception.access.denied"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "common.exception.forbidden"),

    SEND_EMAIL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "common.exception.send.email.failed"),
    GALLERY_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"common.gallery.exception.io"),
    ELASTICSEARCH_NOT_FOUND_INDEX(HttpStatus.INTERNAL_SERVER_ERROR, "common.exception.elasticsearch.not.found.index"),
    IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "common.exception.io");

    @Autowired
    private CommonService commonService;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ServiceError(HttpStatus httpStatus, String message) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.common", locale);

        this.httpStatus = httpStatus;
        this.code = this.name();
        this.message = resourceBundle.getString(message);
    }
}

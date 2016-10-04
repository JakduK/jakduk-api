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

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "exception.invalid.parameter"),
    FORM_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "exception.invalid.parameter"),
    EXPIRATION_TOKEN(HttpStatus.BAD_REQUEST, "exception.expiration.token"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "exception.invalid.token"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "exception.no.such.element"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "exception.not.found.user"),
    NOT_REGISTER_WITH_SNS(HttpStatus.NOT_FOUND, "exception.not.register.with.sns"),
    CANNOT_GET_SNS_PROFILE(HttpStatus.NOT_FOUND, "exception.cannot.get.sns.profile"),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "exception.post.not.found"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "exception.category.not.found"),
    ALREADY_ENABLE(HttpStatus.NOT_FOUND, "exception.already.enable"),
    ALREADY_DISABLE(HttpStatus.NOT_FOUND, "exception.already.disable"),
    NOT_FOUND_FOOTBALL_CLUB(HttpStatus.NOT_FOUND, "exception.football.club.not.found"),
    NOT_FOUND_GALLERY(HttpStatus.NOT_FOUND, "exception.not.found.gallery"),

    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "exception.access.denied"),
    NOT_FOUND_JAKDUK_ACCOUNT(HttpStatus.UNAUTHORIZED, "exception.access.denied"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "exception.forbidden"),

    SEND_EMAIL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "exception.send.email.failed"),
    GALLERY_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "gallery.exception.io"),
    ELASTICSEARCH_NOT_FOUND_INDEX(HttpStatus.INTERNAL_SERVER_ERROR, "exception.elasticsearch.not.found.index"),
    IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "exception.io"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "exception.internal.server.error");

    @Autowired
    private CommonService commonService;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ServiceError(HttpStatus httpStatus, String message) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.exception", locale);

        this.httpStatus = httpStatus;
        this.code = this.name();
        this.message = resourceBundle.getString(message);
    }
}

package com.jakduk.core.exception;

import com.jakduk.core.service.CommonService;
import lombok.Getter;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author pyohwan
 * 16. 7. 1 오후 11:23
 */

@Getter
public enum ServiceError {

    INVALID_PARAMETER(HttpStatus.SC_BAD_REQUEST, "exception.invalid.parameter"),
    FORM_VALIDATION_FAILED(HttpStatus.SC_BAD_REQUEST, "exception.invalid.parameter"),
    EXPIRATION_TOKEN(HttpStatus.SC_BAD_REQUEST, "exception.expiration.token"),
    INVALID_TOKEN(HttpStatus.SC_BAD_REQUEST, "exception.invalid.token"),

    NOT_FOUND(HttpStatus.SC_NOT_FOUND, "exception.no.such.element"),
    NOT_FOUND_USER(HttpStatus.SC_NOT_FOUND, "exception.not.found.user"),
    NOT_REGISTER_WITH_SNS(HttpStatus.SC_NOT_FOUND, "exception.not.register.with.sns"),
    CANNOT_GET_SNS_PROFILE(HttpStatus.SC_NOT_FOUND, "exception.cannot.get.sns.profile"),
    ALREADY_EXIST_EMAIL(HttpStatus.SC_NOT_FOUND, "exception.already.email.exists"),
    ALREADY_EXIST_USERNAME(HttpStatus.SC_NOT_FOUND, "exception.already.username.exists"),

    NOT_FOUND_POST(HttpStatus.SC_NOT_FOUND, "exception.post.not.found"),
    CATEGORY_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "exception.category.not.found"),
    ALREADY_ENABLE(HttpStatus.SC_NOT_FOUND, "exception.already.enable"),
    ALREADY_DISABLE(HttpStatus.SC_NOT_FOUND, "exception.already.disable"),
    NOT_FOUND_FOOTBALL_CLUB(HttpStatus.SC_NOT_FOUND, "exception.football.club.not.found"),
    NOT_FOUND_GALLERY(HttpStatus.SC_NOT_FOUND, "exception.not.found.gallery"),
    NOT_FOUND_GALLERY_FILE(HttpStatus.SC_NOT_FOUND, "exception.not.found.gallery.file"),

    UNAUTHORIZED_ACCESS(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    NEED_TO_LOGIN(HttpStatus.SC_UNAUTHORIZED, "exception.unauthorized"),
    NOT_FOUND_JAKDUK_ACCOUNT(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    NOT_JAKDUK_USER(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    FORBIDDEN(HttpStatus.SC_FORBIDDEN, "exception.forbidden"),

    SEND_EMAIL_FAILED(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.send.email.failed"),
    GALLERY_IO_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "gallery.exception.io"),
    ELASTICSEARCH_NOT_FOUND_INDEX(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.elasticsearch.not.found.index"),
    IO_EXCEPTION(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.io"),
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.internal.server.error");

    @Autowired
    private CommonService commonService;

    private final Integer httpStatus;
    private final String code;
    private final String message;

    ServiceError(Integer httpStatus, String message) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.exception", locale);

        this.httpStatus = httpStatus;
        this.code = this.name();
        this.message = resourceBundle.getString(message);
    }
}

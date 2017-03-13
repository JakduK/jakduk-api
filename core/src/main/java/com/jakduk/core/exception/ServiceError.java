package com.jakduk.core.exception;

import lombok.Getter;
import org.apache.http.HttpStatus;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author pyohwan
 * 16. 7. 1 오후 11:23
 */

@Getter
public enum ServiceError {

    // COMMON
    INVALID_PARAMETER(HttpStatus.SC_BAD_REQUEST, "exception.invalid.parameter"),
    FORM_VALIDATION_FAILED(HttpStatus.SC_BAD_REQUEST, "exception.invalid.parameter"),
    EXPIRATION_TOKEN(HttpStatus.SC_BAD_REQUEST, "exception.expiration.token"),
    INVALID_TOKEN(HttpStatus.SC_BAD_REQUEST, "exception.invalid.token"),
    FEELING_YOU_ARE_WRITER(HttpStatus.SC_BAD_REQUEST, "exception.you.are.writer"),
    FEELING_SELECT_ALREADY_LIKE(HttpStatus.SC_BAD_REQUEST, "exception.select.already.like"),
    NOT_FOUND(HttpStatus.SC_NOT_FOUND, "exception.no.such.element"),
    FORBIDDEN(HttpStatus.SC_FORBIDDEN, "exception.forbidden"),
    IO_EXCEPTION(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.io"),
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.internal.server.error"),

    // FILE
    FILE_ONLY_IMAGE_TYPE_CAN_BE_UPLOADED(HttpStatus.SC_BAD_REQUEST, "exception.only.image.type.can.be.uploaded"),

    // USER
    NOT_FOUND_USER(HttpStatus.SC_NOT_FOUND, "exception.not.found.user"),
    NOT_FOUND_USER_IMAGE(HttpStatus.SC_NOT_FOUND, "exception.not.found.user.image"),
    NOT_REGISTER_WITH_SNS(HttpStatus.SC_NOT_FOUND, "exception.not.register.with.sns"),
    CANNOT_GET_SNS_PROFILE(HttpStatus.SC_NOT_FOUND, "exception.cannot.get.sns.profile"),
    ALREADY_EXIST_EMAIL(HttpStatus.SC_NOT_FOUND, "exception.already.email.exists"),
    ALREADY_EXIST_USERNAME(HttpStatus.SC_NOT_FOUND, "exception.already.username.exists"),

    NOT_FOUND_POST(HttpStatus.SC_NOT_FOUND, "exception.not.found.post"),
    NOT_FOUND_COMMENT(HttpStatus.SC_NOT_FOUND, "exception.not.found.comment"),
    NOT_FOUND_CATEGORY(HttpStatus.SC_NOT_FOUND, "exception.not.found.category"),
    NOT_FOUND_FOOTBALL_CLUB(HttpStatus.SC_NOT_FOUND, "exception.not.found.football.club"),
    NOT_FOUND_GALLERY(HttpStatus.SC_NOT_FOUND, "exception.not.found.gallery"),
    NOT_FOUND_GALLERY_FILE(HttpStatus.SC_NOT_FOUND, "exception.not.found.gallery.file"),
    NOT_FOUND_COMPETITION(HttpStatus.SC_NOT_FOUND, "exception.not.found.competition"),
    NOT_FOUND_ATTENDANCE_LEAGUE(HttpStatus.SC_NOT_FOUND, "exception.not.found.attendance.league"),
    NOT_FOUND_ATTENDANCE_CLUB(HttpStatus.SC_NOT_FOUND, "exception.not.found.attendance.club"),
    NOT_FOUND_FOOTBALL_CLUB_ORIGIN(HttpStatus.SC_NOT_FOUND, "exception.not.found.football.club.origin"),
    NOT_FOUND_ENCYCLOPEDIA(HttpStatus.SC_NOT_FOUND, "exception.not.found.encyclopedia"),
    ALREADY_ENABLE(HttpStatus.SC_NOT_FOUND, "exception.already.enable"),
    ALREADY_DISABLE(HttpStatus.SC_NOT_FOUND, "exception.already.disable"),

    UNAUTHORIZED_ACCESS(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    NEED_TO_LOGIN(HttpStatus.SC_UNAUTHORIZED, "exception.unauthorized"),
    NOT_FOUND_ACCOUNT(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    NOT_JAKDUK_USER(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    BAD_CREDENTIALS(HttpStatus.SC_UNAUTHORIZED, "exception.bad.credentials"),

    SEND_EMAIL_FAILED(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.send.email.failed"),
    GALLERY_IO_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.gallery.io"),
    ELASTICSEARCH_NOT_FOUND_INDEX(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.elasticsearch.not.found.index"),
    ELASTICSEARCH_INDEX_FAILED(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.elasticsearch.index.failed");

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

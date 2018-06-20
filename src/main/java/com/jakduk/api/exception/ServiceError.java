package com.jakduk.api.exception;

import com.jakduk.api.common.util.JakdukUtils;
import lombok.Getter;
import org.apache.http.HttpStatus;

/**
 * @author pyohwan
 * 16. 7. 1 오후 11:23
 */

@Getter
public enum ServiceError {

    // COMMON
    INVALID_PARAMETER(HttpStatus.SC_BAD_REQUEST, "exception.invalid.parameter"),
    FORM_VALIDATION_FAILED(HttpStatus.SC_BAD_REQUEST, "exception.invalid.parameter"),
    FEELING_YOU_ARE_WRITER(HttpStatus.SC_BAD_REQUEST, "exception.you.are.writer"),
    FEELING_SELECT_ALREADY_LIKE(HttpStatus.SC_BAD_REQUEST, "exception.select.already.like"),
    ILLEGAL_ARGUMENT(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.illegal.argument"),
    NOT_FOUND(HttpStatus.SC_NOT_FOUND, "exception.no.such.element"),
    FORBIDDEN(HttpStatus.SC_FORBIDDEN, "exception.forbidden"),
    IO_EXCEPTION(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.io"),
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.internal.server.error"),

    // AUTHENTICATE
    UNAUTHORIZED_ACCESS(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    ANONYMOUS(HttpStatus.SC_UNAUTHORIZED, "exception.anonymous"),
    NEED_TO_LOGIN(HttpStatus.SC_UNAUTHORIZED, "exception.unauthorized"),
    NOT_FOUND_ACCOUNT(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    NOT_JAKDUK_USER(HttpStatus.SC_UNAUTHORIZED, "exception.access.denied"),
    BAD_CREDENTIALS(HttpStatus.SC_UNAUTHORIZED, "exception.bad.credentials"),
    INVALID_ACCOUNT(HttpStatus.SC_UNAUTHORIZED, "exception.invalid.account"),

    // FILE
    FILE_ONLY_IMAGE_TYPE_CAN_BE_UPLOADED(HttpStatus.SC_BAD_REQUEST, "exception.only.image.type.can.be.uploaded"),

    // USER
    NOT_FOUND_USER(HttpStatus.SC_NOT_FOUND, "exception.not.found.user"),
    NOT_FOUND_USER_IMAGE(HttpStatus.SC_NOT_FOUND, "exception.not.found.user.image"),
    NOT_REGISTER_WITH_SNS(HttpStatus.SC_NOT_FOUND, "exception.not.register.with.sns"),
    CANNOT_GET_ATTEMPT_SNS_PROFILE(HttpStatus.SC_NOT_FOUND, "exception.cannot.get.attempt.sns.profile"),

    NOT_FOUND_ARTICLE(HttpStatus.SC_NOT_FOUND, "exception.not.found.post"),
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
    NOT_FOUND_HOMEDESCRIPTION(HttpStatus.SC_NOT_FOUND, "exception.not.found.homedescription"),
    NOT_FOUND_JAKDUSCHEDULE(HttpStatus.SC_NOT_FOUND, "exception.not.found.jakduschedule"),
    NOT_FOUND_JAKDUSCHEDULEGROUP(HttpStatus.SC_NOT_FOUND, "exception.not.found.jakduschedulegroup"),
    ALREADY_ENABLE(HttpStatus.SC_NOT_FOUND, "exception.already.enable"),
    ALREADY_DISABLE(HttpStatus.SC_NOT_FOUND, "exception.already.disable"),

    SEND_EMAIL_FAILED(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.send.email.failed"),
    GALLERY_IO_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.gallery.io"),
    ELASTICSEARCH_NOT_FOUND_INDEX(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.elasticsearch.not.found.index"),
    ELASTICSEARCH_INDEX_FAILED(HttpStatus.SC_INTERNAL_SERVER_ERROR, "exception.elasticsearch.index.failed");

    private final Integer httpStatus;
    private final String code;
    private final String message;

    ServiceError(Integer httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.code = this.name();
        this.message = message;
    }

    public String getMessage() {
        return JakdukUtils.getMessageSource(this.message);
    }

}

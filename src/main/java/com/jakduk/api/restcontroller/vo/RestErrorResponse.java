package com.jakduk.api.restcontroller.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.api.exception.ServiceError;

import java.util.Map;

/**
 * @author pyohwan
 * 16. 3. 5 오전 12:31
 */

public class RestErrorResponse {

    private String code;
    private String message;

    @JsonIgnore
    private Integer httpStatus;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fields;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> detail;

    public RestErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestErrorResponse(String code, String message, Integer httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public RestErrorResponse(ServiceError serviceError) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
        this.httpStatus = serviceError.getHttpStatus();
    }

    public RestErrorResponse(ServiceError serviceError, String message) {
        this.code = serviceError.getCode();
        this.message = message;
        this.httpStatus = serviceError.getHttpStatus();
    }

    public RestErrorResponse(ServiceError serviceError, Map<String, String> fields) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
        this.httpStatus = serviceError.getHttpStatus();
        this.fields = fields;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public Map<String, Object> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, Object> detail) {
        this.detail = detail;
    }
}

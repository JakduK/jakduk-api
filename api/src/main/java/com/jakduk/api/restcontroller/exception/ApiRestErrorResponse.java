package com.jakduk.api.restcontroller.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.core.exception.ServiceError;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author pyohwan
 * 16. 3. 5 오전 12:31
 */

@Getter
public class ApiRestErrorResponse {

    private String code;
    private String message;

    @JsonIgnore
    private Integer httpStatus;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fields;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Setter
    private Map<String, Object> detail;

    public ApiRestErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiRestErrorResponse(String code, String message, Integer httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ApiRestErrorResponse(ServiceError serviceError) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
        this.httpStatus = serviceError.getHttpStatus();
    }

    public ApiRestErrorResponse(ServiceError serviceError, Map<String, String> fields) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
        this.httpStatus = serviceError.getHttpStatus();
        this.fields = fields;
    }
}

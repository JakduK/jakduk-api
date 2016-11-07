package com.jakduk.api.restcontroller.exceptionHandler;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.core.exception.ServiceError;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author pyohwan
 * 16. 3. 5 오전 12:31
 */

@AllArgsConstructor
@Getter
public class RestError {

    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fields;

    public RestError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestError(ServiceError serviceError) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
    }

    public RestError(ServiceError serviceError, Map<String, String> fields) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
        this.fields = fields;
    }
}

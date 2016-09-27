package com.jakduk.api.restcontroller.exceptionHandler;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.exception.ServiceError;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author pyohwan
 * 16. 3. 5 오전 12:31
 */

@Data
@JsonTypeName(value = "error")
@JsonTypeInfo(use= JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestError {

    private String code;
    private String message;
    private List<String> fields;

    public RestError(String message) {
        this.code = CommonConst.RESPONSE_ERROR_DEFAULT_CODE;
        this.message = message;
    }

    public RestError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestError(ServiceError serviceError) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
    }

    public RestError(ServiceError serviceError, List<String> fields) {
        this.code = serviceError.getCode();
        this.message = serviceError.getMessage();
        this.fields = fields;
    }
}

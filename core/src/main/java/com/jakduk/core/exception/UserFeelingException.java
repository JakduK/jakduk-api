package com.jakduk.core.exception;

import lombok.Data;
import lombok.Getter;

/**
 * Created by pyohwan on 16. 3. 26.
 */

public class UserFeelingException extends RuntimeException {

    @Getter
    private String code;

    public UserFeelingException(String code, String message) {
        super(message);
        this.code = code;
    }
}

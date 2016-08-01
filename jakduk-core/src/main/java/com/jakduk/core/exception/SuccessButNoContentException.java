package com.jakduk.core.exception;

/**
 * Created by pyohwan on 16. 5. 6.
 */

// 에러는 아니지만 데이터가 없음.
public class SuccessButNoContentException extends RuntimeException {
    public SuccessButNoContentException(String message) {
        super(message);
    }
}

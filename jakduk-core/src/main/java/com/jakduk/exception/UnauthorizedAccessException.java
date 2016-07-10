package com.jakduk.exception;

/**
 * Created by pyohwan on 16. 3. 5.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

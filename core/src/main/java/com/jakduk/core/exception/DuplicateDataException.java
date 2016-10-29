package com.jakduk.core.exception;

/**
 * Created by pyohwan on 16. 4. 5.
 */
public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String message) {
        super(message);
    }
}

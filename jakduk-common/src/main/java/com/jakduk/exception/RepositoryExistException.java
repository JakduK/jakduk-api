package com.jakduk.exception;

import lombok.AllArgsConstructor;

/**
 * Created by pyohwan on 16. 3. 6.
 */

public class RepositoryExistException extends RuntimeException {
    public RepositoryExistException(String message) {
        super(message);
    }
}

package com.jakduk.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * Created by pyohwan on 16. 6. 16.
 * 가입 회원을 찾을 수 없음.
 */
public class NotFoundJakdukAccountException extends InternalAuthenticationServiceException
{
    public NotFoundJakdukAccountException(String message) {
        super(message);
    }
}

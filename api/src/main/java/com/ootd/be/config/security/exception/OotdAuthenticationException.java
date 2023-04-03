package com.ootd.be.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class OotdAuthenticationException extends AuthenticationException {

    public OotdAuthenticationException(String msg) {
        super(msg);
    }

    public OotdAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

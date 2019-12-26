package com.altimetrik.assignment.core.exceptions;

import javax.security.sasl.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {

    public TokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}

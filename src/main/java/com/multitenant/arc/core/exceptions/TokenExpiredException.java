package com.multitenant.arc.core.exceptions;

import javax.security.sasl.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {

    public TokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}

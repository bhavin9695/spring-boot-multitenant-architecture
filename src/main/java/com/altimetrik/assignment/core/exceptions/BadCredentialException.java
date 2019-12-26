package com.altimetrik.assignment.core.exceptions;

import javax.security.sasl.AuthenticationException;

public class BadCredentialException extends AuthenticationException {
    public BadCredentialException(String errorMessage){
        super(errorMessage);
    }
}

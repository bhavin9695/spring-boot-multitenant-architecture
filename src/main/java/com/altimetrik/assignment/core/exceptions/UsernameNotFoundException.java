package com.altimetrik.assignment.core.exceptions;

import javax.security.sasl.AuthenticationException;

public class UsernameNotFoundException extends AuthenticationException {
    public UsernameNotFoundException(String errorMessage){
        super(errorMessage);
    }
}

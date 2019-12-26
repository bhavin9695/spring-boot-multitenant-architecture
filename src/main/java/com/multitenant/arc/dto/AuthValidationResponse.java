package com.multitenant.arc.dto;

public class AuthValidationResponse {
    private boolean isValidToken;
    private AuthenticationResponse authenticationResponse;

    public AuthValidationResponse(boolean isValidToken, AuthenticationResponse authenticationResponse) {
        this.isValidToken = isValidToken;
        this.authenticationResponse = authenticationResponse;
    }

    public boolean isValidToken() {
        return isValidToken;
    }

    public void setValidToken(boolean validToken) {
        isValidToken = validToken;
    }

    public AuthenticationResponse getAuthenticationResponse() {
        return authenticationResponse;
    }

    public void setAuthenticationResponse(AuthenticationResponse authenticationResponse) {
        this.authenticationResponse = authenticationResponse;
    }
}

package de.mscho.toftws.configuration.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthTokenNotFoundException extends AuthenticationException {
    public AuthTokenNotFoundException(String authToken) {
        super("Could not find user with authToken " + authToken);
    }
}

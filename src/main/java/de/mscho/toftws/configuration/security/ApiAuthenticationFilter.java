package de.mscho.toftws.configuration.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.nio.charset.StandardCharsets;

public class ApiAuthenticationFilter extends AuthenticationFilter {
    private static final String HEADER_KEY = "toft-token";

    public ApiAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager, authenticationConverter());
        setFailureHandler(failureHandler());
        setSuccessHandler(successHandler());
    }

    private static AuthenticationConverter authenticationConverter() {
        return request -> {
            String token = request.getHeader(HEADER_KEY);
            if (token == null) return null;

            return ApiAuthentication.unauthenticated(token);
        };
    }

    private static AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("text/plain;charset=utf8");
            response.getWriter().write(exception.getMessage());
        };
    }

    private static AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // noop
        };
    }
}

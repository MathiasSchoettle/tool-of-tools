package de.mscho.toftws.configuration.security;

import de.mscho.toftws.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiAuthentication authRequest = (ApiAuthentication) authentication;
        var userOptional = userService.getByAuthToken(authRequest.getToken());
        var user = userOptional.orElseThrow(() -> new BadCredentialsException("Invalid api token"));
        return ApiAuthentication.authenticated(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }
}

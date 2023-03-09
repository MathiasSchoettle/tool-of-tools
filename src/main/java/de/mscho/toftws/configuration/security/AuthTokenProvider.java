package de.mscho.toftws.configuration.security;

import de.mscho.toftws.configuration.exception.AuthTokenNotFoundException;
import de.mscho.toftws.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider extends AbstractUserDetailsAuthenticationProvider {
    private final UserService userService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        var authToken = String.valueOf(authentication.getCredentials());
        return userService.getByAuthToken(authToken).orElseThrow(() -> new AuthTokenNotFoundException(authToken));
    }
}

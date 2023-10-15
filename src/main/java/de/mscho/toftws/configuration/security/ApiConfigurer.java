package de.mscho.toftws.configuration.security;

import de.mscho.toftws.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@RequiredArgsConstructor
public class ApiConfigurer extends AbstractHttpConfigurer<ApiConfigurer, HttpSecurity> {

    private final UserService userService;

    @Override
    public void init(HttpSecurity http) {
        http.authenticationProvider(
            new ApiAuthenticationProvider(userService)
        );
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(
                new ApiAuthenticationFilter(authManager),
                AuthorizationFilter.class
        );
    }
}

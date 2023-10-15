package de.mscho.toftws.configuration.security;

import de.mscho.toftws.user.entity.User;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class ApiAuthentication extends AbstractAuthenticationToken {
    @Getter
    private final String token;
    private final User user;

    private ApiAuthentication(User user) {
        // TODO get authorities from real user object
        super(AuthorityUtils.createAuthorityList("Role_user"));
        super.setAuthenticated(true);
        this.user = user;
        this.token = null;
    }

    public ApiAuthentication(String token) {
        super(AuthorityUtils.NO_AUTHORITIES);
        super.setAuthenticated(false);
        this.user = null;
        this.token = token;
    }

    public static ApiAuthentication authenticated(User user) {
        return new ApiAuthentication(user);
    }

    public static ApiAuthentication unauthenticated(String token) {
        return new ApiAuthentication(token);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new RuntimeException("Authentication state of Api is immutable");
    }
}

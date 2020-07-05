package com.github.euler.api.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTAuthenticationToken implements Authentication {

    private static final long serialVersionUID = 1L;

    private final DecodedJWT decodedToken;
    private final String token;

    public JWTAuthenticationToken(DecodedJWT decodedToken, String token) {
        super();
        this.decodedToken = decodedToken;
        this.token = token;
    }

    @Override
    public String getName() {
        return decodedToken.getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return decodedToken.getClaim("roles")
                .asList(String.class).stream()
                .map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return decodedToken.getSubject();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (!isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to untrusted");
        }
    }

    public DecodedJWT getDecodedToken() {
        return decodedToken;
    }

    public String getToken() {
        return token;
    }

}

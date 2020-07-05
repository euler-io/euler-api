package com.github.euler.api.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class EulerAuthentication implements Authentication {

    private static final long serialVersionUID = 1L;

    private final AuthResponse response;

    public EulerAuthentication(AuthResponse response) {
        super();
        this.response = response;
    }

    @Override
    public String getName() {
        return response.getUserName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return response.getRoles().stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
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
        return response.getUserName();
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted");
        }
    }

    public AuthResponse getResponse() {
        return response;
    }

}

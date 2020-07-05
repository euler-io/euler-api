package com.github.euler.api.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class EulerAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authService;

    public EulerAuthenticationProvider(AuthService authService) {
        super();
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        Object credentials = auth.getCredentials();

        if (username == null || credentials == null) {
            throw new BadCredentialsException("Username or credentials not supplied.");
        }
        String password = credentials.toString();

        try {
            AuthResponse resp = authService.authenticate(username, password);
            if (resp != null) {
                return new EulerAuthentication(resp);
            } else {
                throw new BadCredentialsException(username + " not authorized.");
            }
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

}

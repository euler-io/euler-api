package com.github.euler.api.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class EulerAuthenticationProvider implements AuthenticationProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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

        AuthResponse resp = null;
        try {
            LOGGER.info("Authentication attempt for '{}'.", username);
            resp = authService.authenticate(username, password);
        } catch (IOException e) {
            LOGGER.info("Authentication failed for '{}'.", username);
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        if (resp != null) {
            return new EulerAuthentication(resp);
        } else {
            LOGGER.info("Authentication failed for '{}'.", username);
            throw new BadCredentialsException(username + " not authorized.");
        }

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

}

package com.github.euler.api.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.security.JWTAuthenticationFilter;

@Service
public class AuthApiDelegateImpl implements AuthApiDelegate {

    /**
     * @see JWTAuthenticationFilter
     */
    @Override
    public ResponseEntity<String> authenticate(String username, String password) {
        return AuthApiDelegate.super.authenticate(username, password);
    }

}

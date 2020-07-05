package com.github.euler.api.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthApiDelegateImpl implements AuthApiDelegate {

    @Override
    public ResponseEntity<String> authenticate(String username, String password) {
        return AuthApiDelegate.super.authenticate(username, password);
    }

}

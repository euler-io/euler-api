package com.github.euler.api.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;

public interface AuthService {

    AuthResponse authenticate(String username, String password) throws IOException, AuthenticationException;

}

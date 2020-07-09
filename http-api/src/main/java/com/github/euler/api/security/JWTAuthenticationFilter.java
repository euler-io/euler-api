package com.github.euler.api.security;

import static com.github.euler.api.security.SecurityConstants.HEADER_STRING;
import static com.github.euler.api.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final SecurityService securityService;
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(SecurityService securityService, AuthenticationManager authenticationManager) {
        super();
        this.securityService = securityService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication auth) throws IOException, ServletException {
        EulerAuthentication authToken = (EulerAuthentication) auth;
        Builder builder = JWTUtils.buildFromResponse(authToken.getResponse());
        Date expiration = securityService.calculateExpirationFromNow();
        builder.withIssuedAt(new Date()).withExpiresAt(expiration);

        String token = builder.sign(Algorithm.HMAC512(securityService.getSecret()));
        resp.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(TOKEN_PREFIX + token);
    }

}

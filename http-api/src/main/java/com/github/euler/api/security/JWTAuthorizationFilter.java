package com.github.euler.api.security;

import static com.github.euler.api.security.SecurityConstants.HEADER_STRING;
import static com.github.euler.api.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityService securityService;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public JWTAuthorizationFilter(SecurityService securityService, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.securityService = securityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        boolean headerAbsent = header == null || !header.startsWith(TOKEN_PREFIX);
        if (headerAbsent) {
            LOGGER.info("{} missing or not {}..", HEADER_STRING, TOKEN_PREFIX);
            chain.doFilter(req, res);
            return;
        }

        try {
            JWTAuthenticationToken authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
        } catch (JWTVerificationException e) {
            LOGGER.info("{} access token is missing or invalid.", header);
            res.sendError(401, "Access token is missing or invalid");
        }
    }

    private JWTAuthenticationToken getAuthentication(HttpServletRequest request) throws JWTVerificationException {
        String header = request.getHeader(HEADER_STRING);
        if (header != null) {
            return JWTUtils.getAuthentication(securityService.getSecret(), header);
        }

        return null;
    }

}

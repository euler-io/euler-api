package com.github.euler.api.security;

import java.util.Base64;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RequestOptions.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
        super();
    }

    public static RequestOptions buildOptions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof JWTAuthenticationToken) {
            JWTAuthenticationToken jwtAuthentication = (JWTAuthenticationToken) authentication;
            return buildOptions(jwtAuthentication);
        }
        return RequestOptions.DEFAULT;
    }

    public static RequestOptions buildOptions(JWTAuthenticationToken jwtAuthentication) {
        Builder builder = RequestOptions.DEFAULT.toBuilder();
        String token = jwtAuthentication.getToken();
        builder.addHeader(SecurityConstants.AUTHORIZATION_HEADER, SecurityConstants.JWT_TOKEN_PREFIX + " " + token);
        return builder.build();
    }

    public static RequestOptions buildOptions(String username, String password) {
        Builder builder = RequestOptions.DEFAULT.toBuilder();
        String auth = username + ":" + password;
        String token = Base64.getEncoder().encodeToString(auth.getBytes());
        builder.addHeader(SecurityConstants.AUTHORIZATION_HEADER, SecurityConstants.BASIC_TOKEN_PREFIX + token);
        return builder.build();
    }

}

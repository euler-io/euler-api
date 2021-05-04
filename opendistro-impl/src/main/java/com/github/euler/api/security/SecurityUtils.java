package com.github.euler.api.security;

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
        builder.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + " " + token);
        return builder.build();
    }

}

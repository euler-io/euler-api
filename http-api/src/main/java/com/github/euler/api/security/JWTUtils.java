package com.github.euler.api.security;

import static com.github.euler.api.security.SecurityConstants.TOKEN_PREFIX;

import java.util.Base64;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtils {

    public static JWTAuthenticationToken getAuthentication(byte[] secret, String header) throws JWTVerificationException {
        String token = header.replace(TOKEN_PREFIX, "");
        DecodedJWT decodedJWT = getDecodedAndVerifiedJWT(secret, token);
        return new JWTAuthenticationToken(decodedJWT, token);
    }

    public static DecodedJWT getDecodedAndVerifiedJWT(byte[] secret, String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
    }

    public static JWTCreator.Builder buildFromResponse(AuthResponse resp) {
        JWTCreator.Builder builder = JWT.create();
        builder.withSubject(resp.getUserName());
        String[] roles = resp.getRoles().stream().toArray(s -> new String[s]);
        builder.withArrayClaim("roles", roles);
        return builder;
    }

    public static void main(String[] args) {
        System.out.print("Secret: ");
        String secret = args[0];
        System.out.println(Base64.getEncoder().encodeToString(secret.getBytes()));
    }

}

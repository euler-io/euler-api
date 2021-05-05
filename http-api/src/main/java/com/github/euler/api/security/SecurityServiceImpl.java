package com.github.euler.api.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.euler.api.APIConfiguration;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private final APIConfiguration config;

    private byte[] secret;
    private Duration tokenMaxAge;

    @Autowired
    public SecurityServiceImpl(APIConfiguration config) {
        super();
        this.config = config;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        Config securityConfig = config.getConfig().getConfig("euler.http-api.security");
        if (securityConfig.hasPath("secret-file")) {
            try (InputStream input = new FileInputStream(securityConfig.getString("secret-file"))) {
                String secretFileContent = IOUtils.toString(input, "utf-8");
                this.secret = Base64.getMimeDecoder().decode(secretFileContent);
            }
        } else {
            try {
                this.secret = Base64.getDecoder().decode(securityConfig.getString("secret"));
            } catch (ConfigException.Missing e) {
                LOGGER.warn("Secret configuration not found. Generating one.");
                renewSecret();
            }
        }
        this.tokenMaxAge = securityConfig.getDuration("token-max-age");
    }

    @Override
    public void renewSecret() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[512];
            secureRandom.nextBytes(bytes);
            this.secret = bytes;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Date calculateExpirationFromNow() {
        Date expiration = new Date(System.currentTimeMillis() + tokenMaxAge.toMillis());
        return expiration;
    }

    @Override
    public byte[] getSecret() {
        return secret;
    }

    @Override
    public Duration getTokenMaxAge() {
        return tokenMaxAge;
    }

}

package com.github.euler.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.typesafe.config.Config;

@Configuration
public class APIConfigurer implements WebMvcConfigurer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    private final APIConfiguration config;

    @Autowired
    public APIConfigurer(APIConfiguration config) {
        super();
        this.config = config;
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        try {
            String host = config.getConfig().getString("euler.http.host");
            factory.setAddress(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        factory.setPort(config.getConfig().getInt("euler.http.port"));
        if (config.getConfig().getBoolean("euler.http.ssl.enabled")) {
            initSsl(factory);
        }
    }

    private void initSsl(ConfigurableServletWebServerFactory factory) {
        Ssl ssl = new Ssl();

        ssl.setEnabled(true);

        Config sslConfig = config.getConfig().getConfig("euler.http.ssl");
        if (sslConfig.hasPath("key-store")) {
            ssl.setKeyStore(sslConfig.getString("key-store"));
        }
        if (sslConfig.hasPath("key-store-type")) {
            ssl.setKeyStoreType(sslConfig.getString("key-store-type"));
        }
        if (sslConfig.hasPath("key-store-password")) {
            ssl.setKeyStorePassword(sslConfig.getString("key-store-password"));
        }
        if (sslConfig.hasPath("key-alias")) {
            ssl.setKeyAlias(sslConfig.getString("key-alias"));
        }
        if (sslConfig.hasPath("trust-store")) {
            ssl.setTrustStore(sslConfig.getString("trust-store"));
        }
        if (sslConfig.hasPath("trust-store-type")) {
            ssl.setTrustStoreType(sslConfig.getString("trust-store-type"));
        }
        if (sslConfig.hasPath("trust-store-provider")) {
            ssl.setTrustStoreProvider(sslConfig.getString("trust-store-provider"));
        }
        if (sslConfig.hasPath("trust-store-password")) {
            ssl.setTrustStorePassword(sslConfig.getString("trust-store-password"));
        }
        if (sslConfig.hasPath("ciphers")) {
            ssl.setCiphers(sslConfig.getStringList("ciphers").stream().toArray(s -> new String[s]));
        }
        if (sslConfig.hasPath("enabled-protocols")) {
            ssl.setEnabledProtocols(sslConfig.getStringList("enabled-protocols").stream().toArray(s -> new String[s]));
        }

        factory.setSsl(ssl);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> allowedOrigins = config.getConfig().getStringList("euler.http.cors-allowed-origins");
        if (!allowedOrigins.isEmpty()) {
            registry.addMapping("/**").allowedOrigins(allowedOrigins.toArray(new String[allowedOrigins.size()]))
                    .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowCredentials(true);
        }
    }

}

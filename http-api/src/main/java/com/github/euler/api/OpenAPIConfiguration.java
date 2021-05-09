package com.github.euler.api;

import static com.github.euler.api.EulerBanner.getVersion;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfiguration {

    private final APIConfiguration apiConfig;

    @Autowired
    public OpenAPIConfiguration(APIConfiguration config) {
        super();
        this.apiConfig = config;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Config config = apiConfig.getConfig().getConfig("euler.http-api");
        return new OpenAPI()
                .info(
                        new Info()
                                .title(config.getString("info.title"))
                                .description(config.getString("info.description"))
                                .version(getVersion())
                                .license(
                                        new License()
                                                .name(config.getString("info.license.name"))
                                                .url(config.getString("info.license.url"))))
                .schemaRequirement("bearerAuth",
                        new SecurityScheme()
                                .type(Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer"))
                .servers(config.getConfigList("servers").stream()
                        .map(c -> new Server()
                                .description(c.getString("description"))
                                .url(c.getString("url")))
                        .collect(Collectors.toList()));
    }

}

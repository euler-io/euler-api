package com.github.euler.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.swagger.configuration.SwaggerDocumentationConfig;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerAuthConfig extends SwaggerDocumentationConfig {

    public static final String DEFAULT_INCLUDE_PATTERN = "\\/(jobs|job|statistics)\\/?.*";

    @Bean
    @Primary
    @Override
    public Docket customImplementation() {
        return super.customImplementation()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey("euler-auth", "JWT Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{
                new AuthorizationScope("euler-auth", "Euler admin role.")
        };
        return Arrays.asList(
                new SecurityReference("JWT", authorizationScopes));
    }

}

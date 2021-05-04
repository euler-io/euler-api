package com.github.euler.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import io.swagger.configuration.SwaggerDocumentationConfig;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.DefaultPathProvider;
import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
public class SwaggerConfig extends SwaggerDocumentationConfig {

    public static final String DEFAULT_INCLUDE_PATTERN = "\\/(jobs|job|statistics|extensions|template|templates)\\/?.*";

    private final APIConfiguration apiConfiguration;

    @Autowired
    public SwaggerConfig(APIConfiguration apiConfiguration) {
        super();
        this.apiConfiguration = apiConfiguration;
    }

    @Bean
    @Override
    public Docket customImplementation() {
        Docket docket = super.customImplementation()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));

        try {
            URL baseURL = new URL(apiConfiguration.getConfig().getString("euler.http-api.base-url"));
            docket.host(getHost(baseURL));
            docket.pathProvider(new DefaultPathProvider() {

                @Override
                protected String getDocumentationPath() {
                    return baseURL.getPath();
                }

            });
//            docket.
//            specification.host(getHost(baseURL));
//            specification.basePath(baseURL.getPath());
//      specification.scheme(Scheme.forValue(baseURL.getProtocol()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return docket;
    }

    private ApiKey apiKey() {
        return new ApiKey("euler-auth", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[] {
                new AuthorizationScope("euler-auth", "Euler admin role.")
        };
        return Arrays.asList(
                new SecurityReference("JWT", authorizationScopes));
    }

    private String getHost(URL baseURL) {
        String protocol = baseURL.getProtocol();
        String host = baseURL.getHost();
        int port = baseURL.getPort();
        if (port >= -1 && (protocol.equalsIgnoreCase("http") && port != 80) || (protocol.equalsIgnoreCase("https") && port != 443)) {
            host += ":" + port;
        }
        return host;
    }

}

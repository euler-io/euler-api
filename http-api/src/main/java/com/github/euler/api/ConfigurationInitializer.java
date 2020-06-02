package com.github.euler.api;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;

import com.typesafe.config.Config;

public class ConfigurationInitializer implements ApplicationContextInitializer<AnnotationConfigServletWebServerApplicationContext> {

    private final Config config;

    public ConfigurationInitializer(Config config) {
        super();
        this.config = config;
    }

    @Override
    public void initialize(AnnotationConfigServletWebServerApplicationContext ctx) {
        ctx.registerBean(APIConfiguration.class, () -> new APIConfiguration(config));
    }

}

package com.github.euler.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.github.euler.api.handler.Swagger2SpringBoot;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.swagger.configuration.SwaggerDocumentationConfig;

@SpringBootApplication
//@EnableSwagger2
@ComponentScan(basePackages = {"com.github.euler.api", "com.github.euler.api.handler", "io.swagger.configuration"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SwaggerDocumentationConfig.class)})
public class EulerHTTPAPI extends Swagger2SpringBoot {

    private final Config config;

    private ConfigurableApplicationContext ctx;

    public EulerHTTPAPI(Config config) {
        super();
        this.config = config;
    }

    public EulerHTTPAPI() {
        this(ConfigFactory.load());
    }

    public void start(String... args) {
        SpringApplication app = createApp();
        initialize(app);
        ctx = app.run(args);
    }

    protected void initialize(SpringApplication app) {
        app.addInitializers(new ConfigurationInitializer(config));
    }

    protected SpringApplication createApp() {
        SpringApplication app = new SpringApplication(getClass());
        app.setBanner(new EulerBanner());
        return app;
    }

    public void stop() {
        if (ctx != null) {
            ctx.stop();
        }
    }

    public Config getConfig() {
        return config;
    }

}

package com.github.euler.api;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.euler.api.handler.Swagger2SpringBoot;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

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
        return new SpringApplication(getClass());
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

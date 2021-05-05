package com.github.euler.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@SpringBootApplication
public class EulerHttpApiApplication {

	private final Config config;

	private ConfigurableApplicationContext ctx;

	public EulerHttpApiApplication(Config config) {
		super();
		this.config = config;
	}

	public EulerHttpApiApplication() {
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

}
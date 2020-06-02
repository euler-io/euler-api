package com.github.euler.api;

import com.typesafe.config.Config;

public class APIConfiguration {

    private final Config config;

    public APIConfiguration(Config config) {
        super();
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

}

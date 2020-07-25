package com.github.euler.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.typesafe.config.Config;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "com.github.euler.api", "com.github.euler.api.handler", "io.swagger.configuration" })
public class ESEulerHTTPAPI extends EulerHTTPAPI {

    public ESEulerHTTPAPI(Config config) {
        super(config);
    }

    public ESEulerHTTPAPI() {
        super();
    }

    public static void main(String[] args) {
        ESEulerHTTPAPI api = new ESEulerHTTPAPI();
        api.start(args);
    }

}

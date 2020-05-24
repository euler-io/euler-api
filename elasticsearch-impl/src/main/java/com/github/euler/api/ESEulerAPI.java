package com.github.euler.api;

import org.springframework.boot.SpringApplication;

import com.github.euler.api.handler.Swagger2SpringBoot;

public class ESEulerAPI extends Swagger2SpringBoot {

    public static void main(String[] args) {
        new SpringApplication(ESEulerAPI.class).run(args);
    }

}

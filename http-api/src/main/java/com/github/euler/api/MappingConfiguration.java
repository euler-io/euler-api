package com.github.euler.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MappingConfiguration {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
}

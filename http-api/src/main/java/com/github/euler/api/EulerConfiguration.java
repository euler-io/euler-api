package com.github.euler.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.github.euler.configuration.EulerConfigConverter;
import com.github.euler.configuration.EulerExtension;

@Configuration
public class EulerConfiguration {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final List<EulerExtension> serviceExtensions;

    @Autowired
    public EulerConfiguration(List<EulerExtension> serviceExtensions) {
        super();
        this.serviceExtensions = serviceExtensions;
    }

    public EulerConfigConverter getEulerConfigConverter() {
        EulerConfigConverter converter = new EulerConfigConverter();
        serviceExtensions.forEach(e -> converter.register(e));

        LOGGER.info("Found euler extensions:");
        converter.getExtensions().forEach(e -> LOGGER.info(e.getClass().getName() + ": " + e.getDescription()));

        return converter;
    }

}

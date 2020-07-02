package com.github.euler.api;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("com.github.euler.api.WaitElasticsearchBean")
public class InitializeElasticsearchBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeElasticsearchBean.class);

    @PostConstruct
    public void initializeElasticsearch() {
        LOGGER.info("Initiliazing indices.");
    }

}

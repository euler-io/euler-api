package com.github.euler.api.persistence;

import org.elasticsearch.client.RestHighLevelClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;

public abstract class ESPersistence {

    protected final RestHighLevelClient client;
    protected final APIConfiguration configuration;
    protected final ObjectMapper objectMapper;

    public ESPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super();
        this.client = client;
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    protected String getJobIndex() {
        return configuration.getConfig().getString("euler.http-api.elasticsearch.job-index");
    }

}

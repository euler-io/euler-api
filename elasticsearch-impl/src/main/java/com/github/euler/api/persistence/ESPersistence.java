package com.github.euler.api.persistence;

import java.io.IOException;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;

public abstract class ESPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESPersistence.class);

    protected final RestHighLevelClient client;
    protected final APIConfiguration configuration;
    protected final ObjectMapper objectMapper;

    public ESPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super();
        this.client = client;
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    protected void initializeIndex(String index, String jsonMapping, boolean forceCreation, RequestOptions opts) throws IOException {
        if (forceCreation || !isIndexCreated(index, opts)) {
            LOGGER.info("Initiliazing index {}.", index);
            createIndex(index, jsonMapping, opts);
        }
    }

    protected void createIndex(String index, String jsonMapping, RequestOptions opts) throws IOException {
        CreateIndexRequest req = new CreateIndexRequest(index);
        req.mapping(jsonMapping, XContentType.JSON);
        client.indices().create(req, opts);
    }

    protected boolean isIndexCreated(String index, RequestOptions opts) throws IOException {
        GetIndexRequest req = new GetIndexRequest(index);
        return client.indices().exists(req, opts);
    }

}

package com.github.euler.api.persistence;

import java.io.IOException;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.euler.opendistro.OpenDistroClient;

public abstract class BaseESPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseESPersistence.class);

    protected void initializeIndex(OpenDistroClient client, String index, String jsonMapping, RequestOptions opts) throws IOException {
        if (!isIndexCreated(client, index, opts)) {
            LOGGER.info("Initiliazing index {}.", index);
            createIndex(client, index, jsonMapping, opts);
        }
    }

    protected void createIndex(OpenDistroClient client, String index, String jsonMapping, RequestOptions opts) throws IOException {
        CreateIndexRequest req = new CreateIndexRequest(index);
        req.mapping(jsonMapping, XContentType.JSON);
        client.indices().create(req, opts);
    }

    protected boolean isIndexCreated(OpenDistroClient client, String index, RequestOptions opts) throws IOException {
        GetIndexRequest req = new GetIndexRequest(index);
        return client.indices().exists(req, opts);
    }

}

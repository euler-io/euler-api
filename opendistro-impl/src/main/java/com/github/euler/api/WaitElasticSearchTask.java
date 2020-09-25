package com.github.euler.api;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitElasticSearchTask implements Callable<MainResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitElasticSearchTask.class);

    private final RestHighLevelClient client;
    private int maxRetries = 30;
    private long interval = 2000;

    public WaitElasticSearchTask(RestHighLevelClient client) {
        super();
        this.client = client;
    }

    @Override
    public MainResponse call() throws Exception {
        int attempts = 0;
        Exception lastEx = null;
        while (attempts < maxRetries) {
            try {
                MainResponse resp = client.info(RequestOptions.DEFAULT);
                LOGGER.info("Connected to Elasticsearch.");
                return resp;
            } catch (IOException | ElasticsearchException e) {
                LOGGER.warn("Could not connect to Elasticsearch ({}). Retry will occur in {}ms.", e.getMessage(), interval);
                attempts++;
                Thread.sleep(interval);
                lastEx = e;
            }
        }
        throw new IllegalStateException("Could not connect to Elasticsearch.", lastEx);
    }

}

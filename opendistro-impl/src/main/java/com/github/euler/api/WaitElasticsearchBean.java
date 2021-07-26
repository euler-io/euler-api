package com.github.euler.api;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("com.github.euler.api.WaitElasticsearchBean")
public class WaitElasticsearchBean {

    private OpenDistroConfiguration configuration;

    @Autowired
    public WaitElasticsearchBean(OpenDistroConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    @PostConstruct
    public void waitForElasticSearch() throws ExecutionException, IOException {
        RestHighLevelClient client = null;
        try {
            client = configuration.startClient();
            WaitElasticSearchTask task = new WaitElasticSearchTask(client);
            Future<MainResponse> future = Executors.newSingleThreadExecutor().submit(task);
            future.get(3, TimeUnit.MINUTES);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

}

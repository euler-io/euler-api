package com.github.euler.api;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.core.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.euler.opendistro.OpenDistroClient;

@Component("com.github.euler.api.WaitElasticsearchBean")
public class WaitElasticsearchBean {

    private OpenDistroClient client;

    @Autowired
    public WaitElasticsearchBean(OpenDistroClient client) {
        super();
        this.client = client;
    }

    @PostConstruct
    public void waitForElasticSearch() throws ExecutionException {
        WaitElasticSearchTask task = new WaitElasticSearchTask(client);
        Future<MainResponse> future = Executors.newSingleThreadExecutor().submit(task);
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.github.euler.api;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.euler.api.security.SecurityUtils;
import com.github.euler.opendistro.OpenDistroClient;

@Service
@EnableScheduling
@DependsOn("com.github.euler.api.WaitElasticsearchBean")
public class OpenDistroClientManager {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final OpenDistroConfiguration openDistroConfiguration;
    private final OpenDistroClient userClient;
    private final OpenDistroClient adminClient;

    @Autowired
    public OpenDistroClientManager(OpenDistroConfiguration openDistroConfiguration) {
        super();
        this.openDistroConfiguration = openDistroConfiguration;
        this.userClient = openDistroConfiguration.startClient(null, null);
        this.adminClient = openDistroConfiguration.startClient();
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void keepUserClientAlive() {
        ping(userClient);
    }

    @PreDestroy
    public void closeUserClient() throws IOException {
        if (userClient != null) {
            userClient.close();
        }
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void keepAdminClientAlive() {
        ping(adminClient);
    }

    private void ping(RestHighLevelClient client) {
        try {
            String username = openDistroConfiguration.getUsername();
            String password = openDistroConfiguration.getPassword();
            RequestOptions requestOptions = SecurityUtils.buildOptions(username, password);
            client.ping(requestOptions);
        } catch (Exception e) {
            LOGGER.warn("Could not ping elasticsearch: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void closeAdminClient() throws IOException {
        if (adminClient != null) {
            adminClient.close();
        }
    }

    public OpenDistroClient getUserClient() {
        return userClient;
    }

    public OpenDistroClient getAdminClient() {
        return adminClient;
    }

}

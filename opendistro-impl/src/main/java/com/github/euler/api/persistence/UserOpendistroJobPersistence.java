package com.github.euler.api.persistence;

import static com.github.euler.api.security.SecurityUtils.buildOptions;

import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.opendistro.OpenDistroClient;

@Service
public class UserOpendistroJobPersistence extends OpendistroJobPersistence implements UserJobPersistence {

    @Autowired
    public UserOpendistroJobPersistence(OpenDistroClientManager clientManager, APIConfiguration configuration,
            ObjectMapper objectMapper) {
        super(clientManager, configuration, objectMapper);
    }

    @Override
    RequestOptions getRequestOptions() {
        return buildOptions();
    }

    @Override
    protected OpenDistroClient getClient() {
        return clientManager.getUserClient();
    }

}

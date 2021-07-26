package com.github.euler.api.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.opendistro.OpenDistroClient;

@Service
public class AdminOpendistroJobPersistence extends OpendistroJobPersistence implements AdminJobPersistence {

    @Autowired
    public AdminOpendistroJobPersistence(OpenDistroClientManager clientManager,
            APIConfiguration configuration, ObjectMapper objectMapper) {
        super(clientManager, configuration, objectMapper);
    }

    @Override
    protected OpenDistroClient getClient() {
        return clientManager.getAdminClient();
    }

}

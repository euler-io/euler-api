package com.github.euler.api.persistence;

import static com.github.euler.api.security.SecurityUtils.buildOptions;

import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.opendistro.OpenDistroClient;

@Service
public class UserOpendistroJobDetailsPersistence extends OpendistroJobDetailsPersistence
        implements UserJobDetailsPersistence {

    @Autowired
    public UserOpendistroJobDetailsPersistence(OpenDistroClientManager clientManager,
            APIConfiguration configuration) {
        super(clientManager, configuration);
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

package com.github.euler.api.persistence;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.api.model.JobStatistics;
import com.github.euler.opendistro.OpenDistroClient;

@Service
public class UserOpendistroJobStatisticsPersistence extends OpendistroPersistence implements JobStatisticsPersistence {

    public UserOpendistroJobStatisticsPersistence(OpenDistroClientManager clientManager, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(clientManager);
    }

    @Override
    public List<JobStatistics> list(String jobId) {
        return null;
    }

    @Override
    protected OpenDistroClient getClient() {
        return clientManager.getUserClient();
    }

}

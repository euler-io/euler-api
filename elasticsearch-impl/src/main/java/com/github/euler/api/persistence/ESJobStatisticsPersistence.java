package com.github.euler.api.persistence;

import java.util.List;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.JobStatistics;

@Service
public class ESJobStatisticsPersistence extends ESPersistence implements JobStatisticsPersistence {

    public ESJobStatisticsPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration, objectMapper);
    }

    @Override
    public List<JobStatistics> list(String jobId) {
        return null;
    }

}

package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroConfiguration;
import com.github.euler.api.model.JobStatistics;

@Service
public class ESJobStatisticsPersistence extends ESPersistence implements JobStatisticsPersistence {

    public ESJobStatisticsPersistence(OpenDistroConfiguration openDistroConfiguration, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(openDistroConfiguration.startClient(null, null));
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<JobStatistics> list(String jobId) {
        return null;
    }

}

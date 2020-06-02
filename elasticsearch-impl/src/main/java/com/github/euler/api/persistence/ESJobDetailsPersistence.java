package com.github.euler.api.persistence;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;

@Service
public class ESJobDetailsPersistence extends ESPersistence implements JobDetailsPersistence {

    private final ObjectWriter writer;
    private final ObjectReader reader;

    public ESJobDetailsPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration, objectMapper);
        writer = objectMapper.writerFor(Job.class);
        reader = objectMapper.readerFor(JobDetails.class);
    }

    @Override
    public JobDetails get(String id) throws IOException {
        GetRequest req = new GetRequest(getJobIndex(), id);
        GetResponse response = client.get(req, RequestOptions.DEFAULT);
        if (response.isExists()) {
            JobDetails jobDetails = reader.readValue(response.getSourceAsBytes());
            jobDetails.setId(response.getId());
            return jobDetails;
        } else {
            return null;
        }
    }

    @Override
    public JobDetails create(JobDetails job) throws IOException {
        IndexRequest req = new IndexRequest(getJobIndex());
        req.id(job.getId());
        req.source(toBytes(job), XContentType.JSON);
        client.index(req, RequestOptions.DEFAULT);
        return job;
    }

    private byte[] toBytes(JobDetails job) {
        try {
            return writer.writeValueAsBytes(job);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

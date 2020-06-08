package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;

@Service
public class ESJobPersistence extends AbstractJobPersistence<Job> implements JobPersistence {

    private ObjectReader reader;

    public ESJobPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration, objectMapper);
        reader = objectMapper.readerFor(Job.class);
    }

    @Override
    protected Job convert(SearchHit h) {
        Map<String, Object> source = h.getSourceAsMap();
        return objectMapper.convertValue(source, Job.class);
    }

    @Override
    protected Job readValue(byte[] sourceAsBytes) throws IOException {
        return reader.readValue(sourceAsBytes);
    }

    @Override
    public void updateStatus(String id, JobStatus status) throws IOException {
        UpdateRequest req = new UpdateRequest(getJobIndex(), id);
        Map<String, Object> source = new HashMap<>();
        source.put("status", status);
        req.doc(source);
        client.update(req, RequestOptions.DEFAULT);
    }

    @Override
    public void delete(String id) throws IOException {
        DeleteRequest req = new DeleteRequest(getJobIndex(), id);
        client.delete(req, RequestOptions.DEFAULT);
    }

}

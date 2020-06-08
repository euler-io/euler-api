package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;

@Service
public class ESJobDetailsPersistence extends AbstractJobPersistence<JobDetails> implements JobDetailsPersistence {

    private final ObjectWriter writer;
    private final ObjectReader reader;

    public ESJobDetailsPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration, objectMapper);
        writer = objectMapper.writerFor(JobDetails.class);
        reader = objectMapper.readerFor(JobDetails.class);
    }

    @Override
    protected JobDetails readValue(byte[] sourceAsBytes) throws IOException {
        return reader.readValue(sourceAsBytes);
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

    @Override
    protected JobDetails convert(SearchHit h) {
        Map<String, Object> source = h.getSourceAsMap();
        return objectMapper.convertValue(source, JobDetails.class);
    }

    @Override
    public JobDetails getNext() throws IOException {
        return list(0, 1, SortBy.ENQUEUED_DATE, SortDirection.ASC).stream().findFirst().orElse(null);
    }

}

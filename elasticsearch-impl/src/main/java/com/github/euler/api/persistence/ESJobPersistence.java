package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobList;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;
import com.github.euler.opendistro.OpenDistroClient;

@Service
public class ESJobPersistence extends AbstractJobPersistence<Job> implements JobPersistence {

    private final ObjectMapper objectMapper;
    private final ObjectReader reader;

    @Autowired
    public ESJobPersistence(OpenDistroClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration);
        this.objectMapper = objectMapper.copy().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.reader = this.objectMapper.readerFor(Job.class);
    }

    @Override
    public JobList list(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection, JobStatus status) throws IOException {
        SearchResponse response = listJobs(page, size, sortBy, sortDirection, status, true);
        List<Job> jobs = Arrays.stream(response.getHits().getHits())
                .map(h -> convert(h))
                .collect(Collectors.toList());
        int total = Long.valueOf(response.getHits().getTotalHits().value).intValue();

        JobList list = new JobList();
        list.setTotal(total);
        list.setJobs(jobs);
        return list;
    }

    protected Job convert(SearchHit h) {
        Map<String, Object> source = h.getSourceAsMap();
        Job job = objectMapper.convertValue(source, Job.class);
        job.setId(h.getId());
        return job;
    }

    @Override
    protected Job readValue(byte[] sourceAsBytes) throws IOException {
        return reader.readValue(sourceAsBytes);
    }

    @Override
    public void updateStatus(String id, JobStatus status) throws IOException {
        UpdateRequest req = new UpdateRequest(getJobIndex(), id)
                .setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
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

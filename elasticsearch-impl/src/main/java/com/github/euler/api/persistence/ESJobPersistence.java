package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;

@Service
public class ESJobPersistence extends ESPersistence implements JobPersistence {

    public ESJobPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration, objectMapper);
    }

    @Override
    public List<Job> list() throws IOException {
        SearchRequest req = new SearchRequest(getJobIndex());
        MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchSourceBuilder.sort("start-date", SortOrder.ASC);
        SearchResponse response = client.search(req, RequestOptions.DEFAULT);

        return Arrays.stream(response.getHits().getHits())
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private Job convert(SearchHit h) {
        Map<String, Object> source = h.getSourceAsMap();
        return objectMapper.convertValue(source, Job.class);
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

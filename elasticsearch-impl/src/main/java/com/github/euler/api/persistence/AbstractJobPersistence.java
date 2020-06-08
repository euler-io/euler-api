package com.github.euler.api.persistence;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;

public abstract class AbstractJobPersistence<J extends Job> extends ESPersistence {

    public AbstractJobPersistence(RestHighLevelClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration, objectMapper);
    }

    protected SearchResponse listJobs(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection, JobStatus status) throws IOException {
        SearchRequest req = new SearchRequest(getJobIndex());
        QueryBuilder query;
        if (status != null) {
            query = QueryBuilders.termQuery("status", status);
        } else {
            query = QueryBuilders.matchAllQuery();
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchSourceBuilder.size(size);
        searchSourceBuilder.from(page * size);
        searchSourceBuilder.sort(sortBy.toString().toLowerCase(), SortOrder.fromString(sortDirection.toString()));
        return client.search(req, RequestOptions.DEFAULT);
    }

    public J get(String id) throws IOException {
        GetRequest req = new GetRequest(getJobIndex(), id);
        GetResponse response = client.get(req, RequestOptions.DEFAULT);
        if (response.isExists()) {
            J j = readValue(response.getSourceAsBytes());
            j.setId(response.getId());
            return j;
        } else {
            return null;
        }
    }

    protected abstract J readValue(byte[] sourceAsBytes) throws IOException;

}

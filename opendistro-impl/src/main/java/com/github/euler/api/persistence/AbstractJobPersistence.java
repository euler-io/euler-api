package com.github.euler.api.persistence;

import static com.github.euler.api.security.SecurityUtils.buildOptions;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;
import com.github.euler.opendistro.OpenDistroClient;

public abstract class AbstractJobPersistence<J extends Job> extends ESPersistence {

    protected final APIConfiguration configuration;

    public AbstractJobPersistence(OpenDistroClient client, APIConfiguration configuration) {
        super(client);
        this.configuration = configuration;
    }

    protected String getJobIndex() {
        return configuration.getConfig().getString("euler.http-api.elasticsearch.job-index.name");
    }

    protected SearchResponse listJobs(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection, JobStatus status, boolean requestCache) throws IOException {
        SearchRequest req = new SearchRequest(getJobIndex());
        req.requestCache(requestCache);
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
        searchSourceBuilder.sort(sortBy.toString().toLowerCase().replace('_', '-'), SortOrder.fromString(sortDirection.toString()));
        req.source(searchSourceBuilder);
        return client.search(req, buildOptions());
    }

    public J get(String id) throws IOException {
        GetRequest req = new GetRequest(getJobIndex(), id);
        GetResponse response = client.get(req, buildOptions());
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

package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Set;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OpenDistroClientManager;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;

public abstract class AbstractJobPersistence<J extends Job> extends OpendistroPersistence {

    protected final APIConfiguration configuration;

    public AbstractJobPersistence(OpenDistroClientManager clientManager, APIConfiguration configuration) {
        super(clientManager);
        this.configuration = configuration;
    }

    protected String getJobIndex() {
        return configuration.getConfig().getString("euler.http-api.elasticsearch.job-index.name");
    }

    protected SearchResponse listJobs(Integer page, Integer size, SortBy sortBy, SortDirection sortDirection,
            JobStatus[] status, String[] tags, boolean requestCache) throws IOException {
        SearchRequest req = new SearchRequest(getJobIndex());
        req.requestCache(requestCache);
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (status != null && status.length > 0) {
            query.filter(QueryBuilders.termsQuery("status", Set.of(status)));
        }
        if (tags != null && tags.length > 0) {
            query.filter(QueryBuilders.termsQuery("tags", Set.of(tags)));
        }
        if (query.filter().isEmpty()) {
            query.filter(QueryBuilders.matchAllQuery());
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchSourceBuilder.size(size);
        searchSourceBuilder.from(page * size);
        searchSourceBuilder.sort(sortBy.toString().toLowerCase().replace('_', '-'), SortOrder.fromString(sortDirection.toString()));
        req.source(searchSourceBuilder);
        return

        getClient().search(req, getRequestOptions());
    }

    public J get(String id) throws IOException {
        GetRequest req = new GetRequest(getJobIndex(), id);
        GetResponse response = getClient().get(req, getRequestOptions());
        if (response.isExists()) {
            J j = readValue(response.getSourceAsBytes());
            j.setId(response.getId());
            return j;
        } else {
            return null;
        }
    }

    protected abstract J readValue(byte[] sourceAsBytes) throws IOException;

    RequestOptions getRequestOptions() {
        return RequestOptions.DEFAULT;
    }

}

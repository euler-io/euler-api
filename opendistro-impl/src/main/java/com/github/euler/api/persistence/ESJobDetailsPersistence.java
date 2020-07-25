package com.github.euler.api.persistence;

import static com.monitorjbl.json.Match.match;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.euler.api.APIConfiguration;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;
import com.github.euler.opendistro.OpenDistroClient;
import com.monitorjbl.json.JsonView;
import com.typesafe.config.ConfigRenderOptions;

@Service
@DependsOn("com.github.euler.api.WaitElasticsearchBean")
public class ESJobDetailsPersistence extends AbstractJobPersistence<JobDetails> implements JobDetailsPersistence {

    private final ObjectMapper objectMapper;
    private final ObjectReader reader;

    @Autowired
    public ESJobDetailsPersistence(OpenDistroClient client, APIConfiguration configuration, ObjectMapper objectMapper) {
        super(client, configuration);
        this.objectMapper = objectMapper;
        this.reader = this.objectMapper.readerFor(JobDetails.class);
    }

    @PostConstruct
    protected void initializeJobIndex() throws IOException {
        boolean autoInitialize = configuration.getConfig().getBoolean("euler.http-api.elasticsearch.auto-initialize-indices");
        if (autoInitialize) {
            String jsonMapping = configuration.getConfig().getConfig("euler.http-api.elasticsearch.job-index.mappings").root().render(ConfigRenderOptions.concise());
            initializeIndex(getJobIndex(), jsonMapping, RequestOptions.DEFAULT);
        }
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
        req.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
        IndexResponse resp = client.index(req, RequestOptions.DEFAULT);
        job.setId(resp.getId());
        return job;
    }

    private byte[] toBytes(JobDetails job) {
        try {
            return this.objectMapper.writeValueAsBytes(JsonView.with(job).onClass(JobDetails.class, match().exclude("id")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JobDetails getNext() throws IOException {
        SearchHit[] hits = listJobs(0, 10, SortBy.ENQUEUED_DATE, SortDirection.ASC, JobStatus.ENQUEUED, false).getHits().getHits();
        return Arrays.stream(hits)
                .map(h -> convert(h))
                .filter(j -> j.getStatus() == JobStatus.ENQUEUED)
                .findFirst()
                .orElse(null);
    }

    protected JobDetails convert(SearchHit h) {
        Map<String, Object> source = h.getSourceAsMap();
        JobDetails details = objectMapper.convertValue(source, JobDetails.class);
        details.setId(h.getId());
        return details;
    }

}

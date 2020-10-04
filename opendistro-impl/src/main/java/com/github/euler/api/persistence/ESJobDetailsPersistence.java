package com.github.euler.api.persistence;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.github.euler.api.APIConfiguration;
import com.github.euler.api.OffsetDateTimeIO;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.model.SortBy;
import com.github.euler.api.model.SortDirection;
import com.github.euler.opendistro.OpenDistroClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

@Service
@DependsOn("com.github.euler.api.WaitElasticsearchBean")
public class ESJobDetailsPersistence extends AbstractJobPersistence<JobDetails> implements JobDetailsPersistence {

    private final OffsetDateTimeIO.Serializer datetimeSerializer = new OffsetDateTimeIO.Serializer();
    private final OffsetDateTimeIO.Deserializer datetimeDeserializer = new OffsetDateTimeIO.Deserializer();

    @Autowired
    public ESJobDetailsPersistence(OpenDistroClient client, APIConfiguration configuration) {
        super(client, configuration);
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
        Config config = ConfigFactory.parseString(new String(sourceAsBytes, "utf-8"));
        return bind(config);
    }

    protected JobDetails bind(Config config) {
        JobDetails jobDetails = new JobDetails();
        if (config.hasPath("creation-date")) {
            jobDetails.setCreationDate(this.datetimeDeserializer.deserialize(config.getString("creation-date")));
        }
        if (config.hasPath("enqueued-date")) {
            jobDetails.setEnqueuedDate(this.datetimeDeserializer.deserialize(config.getString("enqueued-date")));
        }
        if (config.hasPath("start-date")) {
            jobDetails.setStartDate(this.datetimeDeserializer.deserialize(config.getString("start-date")));
        }
        if (config.hasPath("end-date")) {
            jobDetails.setEndDate(this.datetimeDeserializer.deserialize(config.getString("end-date")));
        }
        jobDetails.setSeed(config.getString("seed"));
        jobDetails.setStatus(JobStatus.fromValue(config.getString("status")));
        jobDetails.setConfig(config.getConfig("config").root().unwrapped());
        return jobDetails;
    }

    @Override
    public JobDetails create(JobDetails job) throws IOException {
        IndexRequest req = new IndexRequest(getJobIndex());
        req.id(job.getId());
        req.source(buildSource(job));
        req.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
        IndexResponse resp = client.index(req, RequestOptions.DEFAULT);
        job.setId(resp.getId());
        return job;
    }

    private Map<String, ?> buildSource(JobDetails job) {
        Map<String, Object> map = new HashMap<>(Map.of(
                "seed", job.getSeed(),
                "status", job.getStatus().name(),
                "config", job.getConfig()));
        if (job.getCreationDate() != null) {
            map.put("creation-date", datetimeSerializer.serialize(job.getCreationDate()));
        }
        if (job.getEnqueuedDate() != null) {
            map.put("enqueued-date", datetimeSerializer.serialize(job.getEnqueuedDate()));
        }
        if (job.getStartDate() != null) {
            map.put("start-date", datetimeSerializer.serialize(job.getStartDate()));
        }
        if (job.getEndDate() != null) {
            map.put("end-date", datetimeSerializer.serialize(job.getEndDate()));
        }
        return map;
    }

    @Override
    public JobDetails getNext() throws IOException {
        SearchHit[] hits = listJobs(0, 1, SortBy.ENQUEUED_DATE, SortDirection.ASC, JobStatus.ENQUEUED, false).getHits().getHits();
        return Arrays.stream(hits)
                .map(h -> convert(h))
                .filter(j -> j.getStatus() == JobStatus.ENQUEUED)
                .findFirst()
                .orElse(null);
    }

    protected JobDetails convert(SearchHit h) {
        Config config = ConfigFactory.parseString(h.getSourceAsString());
        JobDetails jobDetails = bind(config);
        jobDetails.setId(h.getId());
        return jobDetails;
    }

}

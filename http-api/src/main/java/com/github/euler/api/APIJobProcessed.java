package com.github.euler.api;

import java.net.URI;

import com.github.euler.core.JobProcessed;

public class APIJobProcessed extends JobProcessed {

    public final String jobId;

    public APIJobProcessed(String id, URI uri) {
        super(uri);
        this.jobId = id;
    }

    public APIJobProcessed(APIJob job) {
        super(job.uri);
        this.jobId = job.id;
    }

}

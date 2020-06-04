package com.github.euler.api;

import java.net.URI;

public class JobEnqueued implements APICommand {

    public final String jobId;
    public final URI uri;

    public JobEnqueued(String id, URI uri) {
        super();
        this.jobId = id;
        this.uri = uri;
    }

    public JobEnqueued(JobToEnqueue msg) {
        this.jobId = msg.jobId;
        this.uri = msg.uri;
    }

}

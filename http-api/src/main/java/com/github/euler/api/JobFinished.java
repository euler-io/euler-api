package com.github.euler.api;

import java.net.URI;

public class JobFinished implements APICommand {

    public final String jobId;
    public final URI uri;

    public JobFinished(String jobId, URI uri) {
        this.jobId = jobId;
        this.uri = uri;
    }

    public JobFinished(APIJobProcessed msg) {
        this.jobId = msg.jobId;
        this.uri = msg.uri;
    }

}

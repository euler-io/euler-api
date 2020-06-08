package com.github.euler.api;

public class JobEnqueued implements APICommand {

    public final String jobId;

    public JobEnqueued(String id) {
        super();
        this.jobId = id;
    }

    public JobEnqueued(JobToEnqueue msg) {
        this.jobId = msg.jobId;
    }

}

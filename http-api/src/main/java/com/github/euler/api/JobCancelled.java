package com.github.euler.api;

public class JobCancelled implements APICommand {

    public final String jobId;

    public JobCancelled(String jobId) {
        super();
        this.jobId = jobId;
    }

    public JobCancelled(JobToCancel msg) {
        this.jobId = msg.jobId;
    }

}

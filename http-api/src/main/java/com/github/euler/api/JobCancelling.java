package com.github.euler.api;

public class JobCancelling implements APICommand {

    public final String jobId;

    public JobCancelling(JobToCancel msg) {
        this.jobId = msg.jobId;
    }

}

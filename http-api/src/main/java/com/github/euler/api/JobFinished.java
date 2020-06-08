package com.github.euler.api;

public class JobFinished implements APICommand {

    public final String jobId;

    public JobFinished(String jobId) {
        this.jobId = jobId;
    }

    public JobFinished(APIJobProcessed msg) {
        this.jobId = msg.jobId;
    }

}

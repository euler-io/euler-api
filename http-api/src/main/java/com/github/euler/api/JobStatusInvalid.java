package com.github.euler.api;

public class JobStatusInvalid implements APICommand {

    public final String jobId;
    public final String msg;

    public JobStatusInvalid(String jobId, String msg) {
        super();
        this.jobId = jobId;
        this.msg = msg;
    }

}

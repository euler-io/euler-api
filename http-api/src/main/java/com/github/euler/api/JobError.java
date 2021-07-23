package com.github.euler.api;

import com.github.euler.core.JobCommand;

public class JobError implements JobCommand {

    public final String jobId;
    public final String errorMessage;
    public final String errorStack;

    public JobError(APIJob msg, String errorMessage, String errorStack) {
        this.jobId = msg.id;
        this.errorMessage = errorMessage;
        this.errorStack = errorStack;
    }

}

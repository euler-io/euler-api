package com.github.euler.api;

import com.github.euler.core.JobCommand;

public class APIJobCancelled implements JobCommand {

    public final String id;

    public APIJobCancelled(APIJob job) {
        this.id = job.id;
    }

}

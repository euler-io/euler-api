package com.github.euler.api;

import java.net.URI;

import com.github.euler.core.JobProcessed;

public class APIJobProcessed extends JobProcessed {

    public final String id;

    public APIJobProcessed(String id, URI uri) {
        super(uri);
        this.id = id;
    }

    public APIJobProcessed(APIJob job) {
        super(job.uri);
        this.id = job.id;
    }

}

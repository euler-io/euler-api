package com.github.euler.api;

import java.util.HashMap;
import java.util.Map;

public class APIQueueState {

    private final Map<String, JobToEnqueue> mapping = new HashMap<>();

    public void enqueue(JobToEnqueue msg) {
        mapping.put(msg.jobId, msg);
    }

    public JobToEnqueue processed(APIJobProcessed msg) {
        return mapping.get(msg.id);
    }

}

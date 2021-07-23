package com.github.euler.api;

import java.util.HashMap;
import java.util.Map;

public class APIQueueState {

    private final Map<String, JobToEnqueue> mapping = new HashMap<>();
    private int numRunning = 0;

    public void enqueue(JobToEnqueue msg) {
        mapping.put(msg.jobId, msg);
    }

    public JobToEnqueue processed(APIJobProcessed msg) {
        this.numRunning--;
        return mapping.remove(msg.jobId);
    }

    public void running() {
        this.numRunning++;
    }

    public int getNumRunning() {
        return numRunning;
    }

    public JobToEnqueue error(String id, boolean decrease) {
        if (decrease) {
            this.numRunning--;
        }
        return mapping.remove(id);
    }

}

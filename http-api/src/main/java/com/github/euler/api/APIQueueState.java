package com.github.euler.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIQueueState {

    private final Map<String, JobToEnqueue> mapping = new HashMap<>();
    private List<JobToEnqueue> queue = new ArrayList<JobToEnqueue>();
    private int numRunning = 0;

    public void enqueue(JobToEnqueue msg) {
        mapping.put(msg.jobId, msg);
        queue.add(msg);
    }

    public JobToEnqueue processed(APIJobProcessed msg) {
        this.numRunning--;
        return mapping.remove(msg.id);
    }

    public void running() {
        this.numRunning++;
    }

    public JobToEnqueue getHead() {
        return queue.get(0);
    }

    public int getNumRunning() {
        return numRunning;
    }

}

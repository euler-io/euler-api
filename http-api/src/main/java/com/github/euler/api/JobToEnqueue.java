package com.github.euler.api;

import akka.actor.typed.ActorRef;

public class JobToEnqueue implements APICommand {

    public final String jobId;
    public final ActorRef<APICommand> replyTo;

    public JobToEnqueue(String jobId, ActorRef<APICommand> replyTo) {
        super();
        this.jobId = jobId;
        this.replyTo = replyTo;
    }

    public JobToEnqueue(String jobId) {
        super();
        this.jobId = jobId;
        this.replyTo = null;
    }

}

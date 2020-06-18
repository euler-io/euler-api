package com.github.euler.api;

import akka.actor.typed.ActorRef;

public class JobToCancel implements APICommand {

    public final String jobId;
    public final ActorRef<APICommand> replyTo;

    public JobToCancel(String jobId, ActorRef<APICommand> replyTo) {
        super();
        this.jobId = jobId;
        this.replyTo = replyTo;
    }

    public JobToCancel(String jobId) {
        this(jobId, null);
    }

}

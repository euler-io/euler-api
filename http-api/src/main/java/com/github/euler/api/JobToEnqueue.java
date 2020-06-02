package com.github.euler.api;

import java.net.URI;

import com.typesafe.config.Config;

import akka.actor.typed.ActorRef;

public class JobToEnqueue implements APICommand {

    public final String jobId;
    public final URI uri;
    public final Config config;
    public final ActorRef<APICommand> replyTo;

    public JobToEnqueue(String jobId, URI uri, Config config, ActorRef<APICommand> replyTo) {
        super();
        this.jobId = jobId;
        this.uri = uri;
        this.config = config;
        this.replyTo = replyTo;
    }

}

package com.github.euler.api;

import java.net.URI;

import com.github.euler.core.Job;
import com.github.euler.core.JobCommand;

import akka.actor.typed.ActorRef;

public class APIJob extends Job {

    public final String id;

    public APIJob(String id, URI uri, ActorRef<JobCommand> replyTo) {
        super(uri, replyTo);
        this.id = id;
    }

}

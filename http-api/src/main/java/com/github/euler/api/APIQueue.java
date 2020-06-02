package com.github.euler.api;

import java.io.IOException;

import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobPersistence;
import com.github.euler.configuration.EulerConfigConverter;
import com.github.euler.core.JobCommand;
import com.typesafe.config.Config;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;

public class APIQueue extends AbstractBehavior<APICommand> {

    private static final String EULER = "euler-";

    public static Behavior<APICommand> create(int maxJobs, JobPersistence persistence, EulerConfigConverter converter) {
        return Behaviors.setup((ctx) -> new APIQueue(ctx, maxJobs, persistence, converter));
    }

    private int maxJobs;
    private final JobPersistence persistence;
    private final EulerConfigConverter converter;
    private final ActorRef<JobCommand> responseAdaptor;

    private APIQueueState state;

    private APIQueue(ActorContext<APICommand> context, int maxJobs, JobPersistence persistence, EulerConfigConverter converter) {
        super(context);
        this.maxJobs = maxJobs;
        this.persistence = persistence;
        this.converter = converter;
        this.responseAdaptor = context.messageAdapter(JobCommand.class, InternalAdaptedJobCommand::new);
        this.state = new APIQueueState();
    }

    @Override
    public Receive<APICommand> createReceive() {
        ReceiveBuilder<APICommand> builder = newReceiveBuilder();
        builder.onMessage(JobToEnqueue.class, this::onJobToEnqueue);
        builder.onMessage(InternalAdaptedJobCommand.class, this::onInternalAdaptedEulerCommand);
        return builder.build();
    }

    public Behavior<APICommand> onJobToEnqueue(JobToEnqueue msg) throws IOException {
        state.enqueue(msg);
        process(msg);
        persistence.updateStatus(msg.jobId, JobStatus.RUNNING);
        return this;
    }

    private void process(JobToEnqueue msg) {
        ActorRef<JobCommand> ref = spawn(msg.jobId, msg.config);
        APIJob jobMsg = new APIJob(msg.jobId, msg.uri, responseAdaptor);
        ref.tell(jobMsg);
    }

    private ActorRef<JobCommand> spawn(String jobId, Config config) {
        Behavior<JobCommand> behavior = converter.create(config, (s, p) -> APIJobExecution.create(s, p));
        String name = EULER + jobId;
        return getContext().spawn(behavior, name);
    }

    private Behavior<APICommand> onInternalAdaptedEulerCommand(InternalAdaptedJobCommand msg) throws IOException {
        if (msg.response instanceof APIJobProcessed) {
            return onJobProcessed((APIJobProcessed) msg.response);
        } else {
            throw new IllegalArgumentException("Unknown response: " + msg.response.getClass().getName());
        }
    }

    private Behavior<APICommand> onJobProcessed(APIJobProcessed msg) throws IOException {
        JobToEnqueue original = state.processed(msg);
        persistence.updateStatus(msg.id, JobStatus.FINISHED);
        original.replyTo.tell(new JobFinished(msg.id, msg.uri));
        return this;
    }

    private static class InternalAdaptedJobCommand implements APICommand {

        private final JobCommand response;

        public InternalAdaptedJobCommand(JobCommand msg) {
            this.response = msg;
        }

    }

}

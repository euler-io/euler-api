package com.github.euler.api;

import com.github.euler.core.CancelJob;
import com.github.euler.core.EulerCommand;
import com.github.euler.core.EulerJobProcessor;
import com.github.euler.core.JobCommand;
import com.github.euler.core.JobProcessed;
import com.github.euler.core.JobToProcess;
import com.github.euler.core.ProcessorCommand;
import com.github.euler.core.SourceCommand;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;

public class APIJobExecution extends AbstractBehavior<JobCommand> {

    public static Behavior<JobCommand> create(Behavior<SourceCommand> sourceBehavior, Behavior<ProcessorCommand> processorBehavior) {
        return Behaviors.setup((context) -> new APIJobExecution(context, sourceBehavior, processorBehavior));
    }

    private final Behavior<SourceCommand> sourceBehavior;
    private final Behavior<ProcessorCommand> processorBehavior;

    private APIJob job;
    private ActorRef<EulerCommand> eulerRef;

    private APIJobExecution(ActorContext<JobCommand> context, Behavior<SourceCommand> sourceBehavior, Behavior<ProcessorCommand> processorBehavior) {
        super(context);
        this.sourceBehavior = sourceBehavior;
        this.processorBehavior = processorBehavior;
    }

    @Override
    public Receive<JobCommand> createReceive() {
        ReceiveBuilder<JobCommand> builder = newReceiveBuilder();
        builder.onMessage(APIJob.class, this::onJob);
        builder.onMessage(JobProcessed.class, this::onJobProcessed);
        builder.onMessage(CancelJob.class, this::onCancelJob);
        builder.onMessage(InternalJobCancelled.class, this::onInternalJobCancelled);
        return builder.build();
    }

    private Behavior<JobCommand> onJob(APIJob msg) {
        this.job = msg;
        this.eulerRef = getContext().spawn(EulerJobProcessor.create(sourceBehavior, processorBehavior), "euler");
        eulerRef.tell(new JobToProcess(msg.uri, getContext().getSelf()));
        return Behaviors.same();
    }

    private Behavior<JobCommand> onJobProcessed(JobProcessed msg) {
        job.replyTo.tell(new APIJobProcessed(job));
        return Behaviors.stopped();
    }

    private Behavior<JobCommand> onCancelJob(CancelJob msg) {
        getContext().watchWith(this.eulerRef, new InternalJobCancelled());
        return Behaviors.stopped();
    }

    private Behavior<JobCommand> onInternalJobCancelled(InternalJobCancelled msg) {
        job.replyTo.tell(new APIJobCancelled(job));
        return Behaviors.same();
    }

    private static class InternalJobCancelled implements JobCommand {

    }

}

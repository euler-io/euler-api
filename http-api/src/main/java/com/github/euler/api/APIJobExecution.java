package com.github.euler.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.euler.core.EulerCommand;
import com.github.euler.core.EulerHooks;
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

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static Behavior<JobCommand> create(Behavior<SourceCommand> sourceBehavior, Behavior<ProcessorCommand> processorBehavior, EulerHooks hooks) {
        return Behaviors.setup((context) -> new APIJobExecution(context, sourceBehavior, processorBehavior, hooks));
    }

    public static Behavior<JobCommand> create(Behavior<SourceCommand> sourceBehavior, Behavior<ProcessorCommand> processorBehavior) {
        return Behaviors.setup((context) -> new APIJobExecution(context, sourceBehavior, processorBehavior));
    }

    private final Behavior<SourceCommand> sourceBehavior;
    private final Behavior<ProcessorCommand> processorBehavior;
    private final EulerHooks hooks;

    private APIJob job;
    private ActorRef<EulerCommand> eulerRef;

    private APIJobExecution(ActorContext<JobCommand> context, Behavior<SourceCommand> sourceBehavior, Behavior<ProcessorCommand> processorBehavior, EulerHooks hooks) {
        super(context);
        this.sourceBehavior = sourceBehavior;
        this.processorBehavior = processorBehavior;
        this.hooks = hooks;
    }

    private APIJobExecution(ActorContext<JobCommand> context, Behavior<SourceCommand> sourceBehavior, Behavior<ProcessorCommand> processorBehavior) {
        this(context, sourceBehavior, processorBehavior, new EulerHooks());
    }

    @Override
    public Receive<JobCommand> createReceive() {
        ReceiveBuilder<JobCommand> builder = newReceiveBuilder();
        builder.onMessage(APIJob.class, this::onJob);
        builder.onMessage(JobProcessed.class, this::onJobProcessed);
        return builder.build();
    }

    private Behavior<JobCommand> onJob(APIJob msg) throws IOException {
        try {
            this.hooks.initialize();
            this.job = msg;
            this.eulerRef = getContext().spawn(EulerJobProcessor.create(sourceBehavior, processorBehavior), "euler");
            eulerRef.tell(new JobToProcess(msg.uri, getContext().getSelf()));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.replyTo.tell(new JobError(msg, e.getMessage(), sw.toString()));
            LOGGER.warn("Error initializing job " + msg.id + ".", e);
        }
        return Behaviors.same();
    }

    private Behavior<JobCommand> onJobProcessed(JobProcessed msg) throws IOException {
        job.replyTo.tell(new APIJobProcessed(job));
        try {
            this.hooks.close();
        } catch (Exception e) {
            LOGGER.warn("Error closing job " + job.id + ".", e);
        }
        return Behaviors.stopped();
    }

}

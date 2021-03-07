package com.github.euler.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobDetailsPersistence;
import com.github.euler.api.persistence.JobPersistence;
import com.github.euler.configuration.EulerConfigConverter;
import com.github.euler.core.JobCommand;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;

public class APIQueue extends AbstractBehavior<APICommand> {

    private static final String EULER = "euler-";

    public static Behavior<APICommand> create(int maxJobs, JobPersistence persistence, JobDetailsPersistence detailsPersistence, ObjectMapper mapper,
            EulerConfigConverter converter) {
        return Behaviors.setup((ctx) -> new APIQueue(ctx, maxJobs, persistence, detailsPersistence, mapper, converter));
    }

    private int maxJobs;
    private final JobPersistence persistence;
    private final JobDetailsPersistence detailsPersistence;
    private final ObjectMapper mapper;
    private final EulerConfigConverter converter;
    private final ActorRef<JobCommand> responseAdaptor;

    private APIQueueState state;

    private APIQueue(ActorContext<APICommand> context, int maxJobs, JobPersistence persistence, JobDetailsPersistence detailsPersistence, ObjectMapper mapper,
            EulerConfigConverter converter) {
        super(context);
        this.maxJobs = maxJobs;
        this.persistence = persistence;
        this.detailsPersistence = detailsPersistence;
        this.mapper = mapper;
        this.converter = converter;
        this.responseAdaptor = context.messageAdapter(JobCommand.class, InternalAdaptedJobCommand::new);
        this.state = new APIQueueState();
    }

    @Override
    public Receive<APICommand> createReceive() {
        ReceiveBuilder<APICommand> builder = newReceiveBuilder();
        builder.onMessage(StartQueue.class, this::onStartQueue);
        builder.onMessage(JobToCancel.class, this::onJobToCancel);
        builder.onMessage(JobToEnqueue.class, this::onJobToEnqueue);
        builder.onMessage(InternalAdaptedJobCommand.class, this::onInternalAdaptedEulerCommand);
        builder.onMessage(InternalJobCancelled.class, this::onInternalJobCancelled);
        return builder.build();
    }

    public Behavior<APICommand> onStartQueue(StartQueue msg) throws IOException, URISyntaxException {
        if (isSpotAvailable()) {
            processNext();
        }
        return this;
    }

    public Behavior<APICommand> onJobToCancel(JobToCancel msg) throws IOException {
        Job job = persistence.get(msg.jobId);
        JobStatus status = job.getStatus();
        if (status != JobStatus.CANCELLED && status != JobStatus.CANCELLING && status != JobStatus.FINISHED && status != JobStatus.ERROR) {
            startCancel(msg);
        } else if (msg.replyTo != null) {
            msg.replyTo.tell(new JobStatusInvalid(msg.jobId, "Impossible to cancel job with status " + status));
        }
        return this;
    }

    private void startCancel(JobToCancel msg) throws IOException {
        persistence.updateStatus(msg.jobId, JobStatus.CANCELLING);
        if (msg.replyTo != null) {
            msg.replyTo.tell(new JobCancelling(msg));
        }
        Optional<ActorRef<Void>> child = getContext().getChild(getChildName(msg.jobId));
        if (child.isPresent()) {
            ActorRef<Void> other = child.get();
            getContext().watchWith(other, new InternalJobCancelled(msg));
            getContext().stop(other);
        } else {
            jobCancelled(msg);
        }
    }

    public Behavior<APICommand> onInternalJobCancelled(InternalJobCancelled imsg) throws IOException {
        jobCancelled(imsg.msg);
        return this;
    }

    protected void jobCancelled(JobToCancel msg) throws IOException {
        persistence.updateStatus(msg.jobId, JobStatus.CANCELLED);
        if (msg.replyTo != null) {
            msg.replyTo.tell(new JobCancelled(msg));
        }
    }

    public Behavior<APICommand> onJobToEnqueue(JobToEnqueue msg) throws IOException, URISyntaxException {
        Job job = persistence.get(msg.jobId);
        JobStatus status = job.getStatus();
        if (status == JobStatus.NEW) {
            state.enqueue(msg);
            persistence.updateEnqueued(msg.jobId);
            if (msg.replyTo != null) {
                msg.replyTo.tell(new JobEnqueued(msg));
            }
            scheduleProcessNext();
        } else if (msg.replyTo != null) {
            msg.replyTo.tell(new JobStatusInvalid(msg.jobId, "Impossible to enqueue job with status " + status));
        }
        return this;
    }

    private void process(JobDetails jobDetails) throws IOException, URISyntaxException {
        Config config = getConfig(jobDetails);
        URI uri = new URI(jobDetails.getSeed());
        ActorRef<JobCommand> ref = spawn(jobDetails.getId(), config);
        state.running();
        persistence.updateRunning(jobDetails.getId());
        APIJob jobMsg = new APIJob(jobDetails.getId(), uri, responseAdaptor);
        ref.tell(jobMsg);
    }

    private Config getConfig(JobDetails jobDetails) throws IOException {
        Object rawConfig = jobDetails.getConfig();
        String json = mapper.writer().writeValueAsString(rawConfig);
        return ConfigFactory.parseString(json);
    }

    private ActorRef<JobCommand> spawn(String jobId, Config config) {
        Behavior<JobCommand> behavior = create(config);
        String name = getChildName(jobId);
        return getContext().spawn(behavior, name);
    }

    private Behavior<JobCommand> create(Config config) {
        ConfigValue value;
        try {
            value = config.getList("config");
        } catch (ConfigException.WrongType e) {
            value = config.getValue("config");
        }
        return converter.create(value, (s, p, h) -> APIJobExecution.create(s, p, h));
    }

    protected String getChildName(String jobId) {
        return EULER + jobId;
    }

    private Behavior<APICommand> onInternalAdaptedEulerCommand(InternalAdaptedJobCommand msg) throws IOException, URISyntaxException {
        if (msg.response instanceof APIJobProcessed) {
            return onJobProcessed((APIJobProcessed) msg.response);
        } else {
            throw new IllegalArgumentException("Unknown response: " + msg.response.getClass().getName());
        }
    }

    private Behavior<APICommand> onJobProcessed(APIJobProcessed msg) throws IOException, URISyntaxException {
        persistence.updateFinished(msg.jobId);
        JobToEnqueue original = state.processed(msg);
        if (original != null && original.replyTo != null) {
            original.replyTo.tell(new JobFinished(msg));
        }
        scheduleProcessNext();
        return this;
    }

    private void processNext() throws IOException, URISyntaxException {
        getContext().getLog().info("Looking for new job to run in the queue.");
        JobDetails next = detailsPersistence.getNext();
        if (next != null) {
            getContext().getLog().info("Found job {} in the queue.", next.getId());
            process(next);
        } else {
            getContext().getLog().info("No jobs found in the queue.");
        }
    }

    private void scheduleProcessNext() {
        getContext().getSelf().tell(new StartQueue());
    }

    private boolean isSpotAvailable() {
        return this.state.getNumRunning() < this.maxJobs;
    }

    private static class InternalAdaptedJobCommand implements APICommand {

        private final JobCommand response;

        public InternalAdaptedJobCommand(JobCommand msg) {
            this.response = msg;
        }

    }

    private static class InternalJobCancelled implements APICommand {

        public final JobToCancel msg;

        public InternalJobCancelled(JobToCancel msg) {
            this.msg = msg;
        }

    }

}

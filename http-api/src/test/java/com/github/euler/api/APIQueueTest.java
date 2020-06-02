package com.github.euler.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.junit.Test;

import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobPersistence;
import com.github.euler.configuration.EulerConfigConverter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;

public class APIQueueTest extends AkkaTest {

    private EulerConfigConverter converter = new EulerConfigConverter();

    @Test
    public void testEnqueueJob() throws Exception {
        int maxJobs = 1;
        String jobId = "0";
        Config config = ConfigFactory.parseString("{source: empty, tasks: []}");

        TestProbe<APICommand> probe = testKit.createTestProbe();
        JobToEnqueue msg = new JobToEnqueue(jobId, new URI("file:///some/path"), config, probe.ref());
        JobPersistence persistence = mock(JobPersistence.class);
        persistence.updateStatus(jobId, JobStatus.RUNNING);
        persistence.updateStatus(jobId, JobStatus.FINISHED);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(maxJobs, persistence, converter));
        ref.tell(msg);

        probe.expectMessageClass(JobFinished.class);
        verify(persistence);
    }

}

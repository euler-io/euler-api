package com.github.euler.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        persistence.updateStatus(eq(jobId), eq(JobStatus.ENQUEUED));
        persistence.updateStatus(eq(jobId), eq(JobStatus.RUNNING));
        persistence.updateStatus(eq(jobId), eq(JobStatus.FINISHED));

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(maxJobs, 100, persistence, converter));

        JobToEnqueue msg = new JobToEnqueue(jobId, new URI("file:///some/path"), config, probe.ref());
        ref.tell(msg);

        probe.expectMessageClass(JobEnqueued.class);
        probe.expectMessageClass(JobFinished.class);

        // verify mock
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.ENQUEUED));
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.RUNNING));
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.FINISHED));
    }

    @Test
    public void testEnqueueJobsAndHitMaxJobs() throws Exception {
        int maxJobs = 1;
        String jobId1 = "0";
        String jobId2 = "1";

        Config config = ConfigFactory.parseString("{source: empty, tasks: []}");

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        persistence.updateStatus(eq(jobId1), eq(JobStatus.ENQUEUED));
        persistence.updateStatus(eq(jobId1), eq(JobStatus.RUNNING));
        persistence.updateStatus(eq(jobId1), eq(JobStatus.FINISHED));
        persistence.updateStatus(eq(jobId2), eq(JobStatus.ENQUEUED));
        persistence.updateStatus(eq(jobId2), eq(JobStatus.RUNNING));
        persistence.updateStatus(eq(jobId2), eq(JobStatus.FINISHED));

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(maxJobs, 100, persistence, converter));

        JobToEnqueue msg1 = new JobToEnqueue(jobId1, new URI("file:///some/path"), config, probe.ref());
        JobToEnqueue msg2 = new JobToEnqueue(jobId2, new URI("file:///some/path"), config, probe.ref());
        ref.tell(msg1);
        assertEquals(jobId1, probe.expectMessageClass(JobEnqueued.class).jobId);
        ref.tell(msg2);
        assertEquals(jobId2, probe.expectMessageClass(JobEnqueued.class).jobId);

        assertEquals(jobId1, probe.expectMessageClass(JobFinished.class).jobId);
        // verify mock
        verify(persistence).updateStatus(eq(jobId1), eq(JobStatus.ENQUEUED));
        verify(persistence).updateStatus(eq(jobId1), eq(JobStatus.RUNNING));
        verify(persistence).updateStatus(eq(jobId1), eq(JobStatus.FINISHED));

        assertEquals(jobId2, probe.expectMessageClass(JobFinished.class).jobId);
        // verify mock
        verify(persistence, times(1)).updateStatus(eq(jobId2), eq(JobStatus.ENQUEUED));
        verify(persistence, times(1)).updateStatus(eq(jobId2), eq(JobStatus.RUNNING));
        verify(persistence, times(1)).updateStatus(eq(jobId2), eq(JobStatus.FINISHED));

    }

}

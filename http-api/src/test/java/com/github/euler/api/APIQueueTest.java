package com.github.euler.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.JobStatus;
import com.github.euler.api.persistence.JobDetailsPersistence;
import com.github.euler.api.persistence.JobPersistence;
import com.github.euler.configuration.EulerConfigConverter;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;

public class APIQueueTest extends AkkaTest {

    private EulerConfigConverter converter = new EulerConfigConverter();
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEnqueueAndRunJob() throws Exception {
        int maxJobs = 1;
        String jobId = "0";
        String config = "{\"source\": \"empty\", \"tasks\": []}";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.NEW);
        when(persistence.get(jobId)).thenReturn(job);
        persistence.updateStatus(eq(jobId), eq(JobStatus.ENQUEUED));
        persistence.updateStatus(eq(jobId), eq(JobStatus.RUNNING));
        persistence.updateStatus(eq(jobId), eq(JobStatus.FINISHED));

        JobDetailsPersistence detailsPersistence = mock(JobDetailsPersistence.class);
        JobDetails jobDetails = new JobDetails();
        jobDetails.setId(jobId);
        jobDetails.setSeed("file:///some/path");
        jobDetails.setConfig(new ObjectMapper().readerFor(HashMap.class).readValue(config));
        when(detailsPersistence.getNext()).thenReturn(jobDetails).thenReturn(null);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(maxJobs, persistence, detailsPersistence, mapper, converter));

        JobToEnqueue msg = new JobToEnqueue(jobId, probe.ref());
        ref.tell(msg);

        probe.expectMessageClass(JobEnqueued.class);
        probe.expectMessageClass(JobFinished.class);

        // verify mock
        verify(persistence).get(jobId);
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.ENQUEUED));
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.RUNNING));
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.FINISHED));
        verify(detailsPersistence, times(2)).getNext();
    }

    @Test
    public void testEnqueueJobsAndHitMaxJobs() throws Exception {
        int maxJobs = 1;
        String jobId1 = "0";
        String jobId2 = "1";
        String config = "{\"source\": \"empty\", \"tasks\": []}";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job1 = new Job();
        job1.setId(jobId1);
        job1.setStatus(JobStatus.NEW);
        when(persistence.get(jobId1)).thenReturn(job1);
        Job job2 = new Job();
        job2.setId(jobId2);
        job2.setStatus(JobStatus.NEW);
        when(persistence.get(jobId2)).thenReturn(job2);
        persistence.updateStatus(eq(jobId1), eq(JobStatus.ENQUEUED));
        persistence.updateStatus(eq(jobId1), eq(JobStatus.RUNNING));
        persistence.updateStatus(eq(jobId1), eq(JobStatus.FINISHED));
        persistence.updateStatus(eq(jobId2), eq(JobStatus.ENQUEUED));
        persistence.updateStatus(eq(jobId2), eq(JobStatus.RUNNING));
        persistence.updateStatus(eq(jobId2), eq(JobStatus.FINISHED));

        JobDetailsPersistence detailsPersistence = mock(JobDetailsPersistence.class);
        JobDetails jobDetails1 = new JobDetails();
        jobDetails1.setId(jobId1);
        jobDetails1.setSeed("file:///some/path1");
        jobDetails1.setConfig(new ObjectMapper().readerFor(HashMap.class).readValue(config));
        when(detailsPersistence.get(jobId1)).thenReturn(jobDetails1);
        JobDetails jobDetails2 = new JobDetails();
        jobDetails2.setId(jobId2);
        jobDetails2.setSeed("file:///some/path2");
        jobDetails2.setConfig(new ObjectMapper().readerFor(HashMap.class).readValue(config));
        when(detailsPersistence.getNext()).thenReturn(jobDetails1).thenReturn(jobDetails2).thenReturn(null);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(maxJobs, persistence, detailsPersistence, mapper, converter));

        JobToEnqueue msg1 = new JobToEnqueue(jobId1, probe.ref());
        JobToEnqueue msg2 = new JobToEnqueue(jobId2, probe.ref());
        ref.tell(msg1);
        assertEquals(jobId1, probe.expectMessageClass(JobEnqueued.class).jobId);
        ref.tell(msg2);
        assertEquals(jobId2, probe.expectMessageClass(JobEnqueued.class).jobId);

        assertEquals(jobId1, probe.expectMessageClass(JobFinished.class).jobId);
        // verify mock
        verify(persistence).get(jobId1);
        verify(persistence).updateStatus(eq(jobId1), eq(JobStatus.ENQUEUED));
        verify(persistence).updateStatus(eq(jobId1), eq(JobStatus.RUNNING));
        verify(persistence).updateStatus(eq(jobId1), eq(JobStatus.FINISHED));

        assertEquals(jobId2, probe.expectMessageClass(JobFinished.class).jobId);
        // verify mock
        verify(persistence).get(jobId2);
        verify(persistence).updateStatus(eq(jobId2), eq(JobStatus.ENQUEUED));
        verify(persistence).updateStatus(eq(jobId2), eq(JobStatus.RUNNING));
        verify(persistence).updateStatus(eq(jobId2), eq(JobStatus.FINISHED));
        verify(detailsPersistence, times(3)).getNext();
    }

    @Test
    public void testEnqueueNotNewJob() throws Exception {
        int maxJobs = 1;
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.RUNNING);
        when(persistence.get(jobId)).thenReturn(job);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(maxJobs, persistence, mock(JobDetailsPersistence.class), mapper, converter));

        JobToEnqueue msg = new JobToEnqueue(jobId, probe.ref());
        ref.tell(msg);
        assertEquals(jobId, probe.expectMessageClass(JobStatusInvalid.class).jobId);

        // verify mock
        verify(persistence).get(jobId);
    }

    @Test
    public void testCancelNewJob() throws Exception {
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.NEW);
        when(persistence.get(jobId)).thenReturn(job);
        persistence.updateStatus(eq(jobId), eq(JobStatus.CANCELLED));

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, mock(JobDetailsPersistence.class), mapper, converter));
        ref.tell(new JobToCancel(jobId, probe.ref()));
        assertEquals(jobId, probe.expectMessageClass(JobCancelling.class).jobId);
        assertEquals(jobId, probe.expectMessageClass(JobCancelled.class).jobId);

        verify(persistence).get(jobId);
        verify(persistence, times(1)).updateStatus(eq(jobId), eq(JobStatus.CANCELLED));
    }

    @Test
    public void testCancelEnqueuedJob() throws Exception {
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.ENQUEUED);
        when(persistence.get(jobId)).thenReturn(job);
        persistence.updateStatus(eq(jobId), eq(JobStatus.CANCELLED));

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, mock(JobDetailsPersistence.class), mapper, converter));
        ref.tell(new JobToCancel(jobId, probe.ref()));
        assertEquals(jobId, probe.expectMessageClass(JobCancelling.class).jobId);
        assertEquals(jobId, probe.expectMessageClass(JobCancelled.class).jobId);

        verify(persistence).get(jobId);
        verify(persistence, times(1)).updateStatus(eq(jobId), eq(JobStatus.CANCELLED));
    }

    @Test
    public void testCancelRunningJob() throws Exception {
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.RUNNING);
        when(persistence.get(jobId)).thenReturn(job);
        persistence.updateStatus(eq(jobId), eq(JobStatus.CANCELLED));

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, mock(JobDetailsPersistence.class), mapper, converter));
        ref.tell(new JobToCancel(jobId, probe.ref()));
        assertEquals(jobId, probe.expectMessageClass(JobCancelling.class).jobId);
        assertEquals(jobId, probe.expectMessageClass(JobCancelled.class).jobId);

        verify(persistence).get(jobId);
        verify(persistence, times(1)).updateStatus(eq(jobId), eq(JobStatus.CANCELLED));
    }

    public void testCancelCancelledJob() throws Exception {
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.CANCELLED);
        when(persistence.get(jobId)).thenReturn(job);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, mock(JobDetailsPersistence.class), mapper, converter));
        ref.tell(new JobToCancel(jobId, probe.ref()));
        assertEquals(jobId, probe.expectMessageClass(JobStatusInvalid.class).jobId);

        verify(persistence).get(jobId);
    }

    @Test
    public void testCancelFinishedJob() throws Exception {
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.FINISHED);
        when(persistence.get(jobId)).thenReturn(job);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, mock(JobDetailsPersistence.class), mapper, converter));
        ref.tell(new JobToCancel(jobId, probe.ref()));
        assertEquals(jobId, probe.expectMessageClass(JobStatusInvalid.class).jobId);

        verify(persistence).get(jobId);
    }

    @Test
    public void testCancelErroredJob() throws Exception {
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.ERROR);
        when(persistence.get(jobId)).thenReturn(job);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, mock(JobDetailsPersistence.class), mapper, converter));
        ref.tell(new JobToCancel(jobId, probe.ref()));
        assertEquals(jobId, probe.expectMessageClass(JobStatusInvalid.class).jobId);

        verify(persistence).get(jobId);
    }

    @Test
    public void testCancelCancellingJob() throws Exception {
        String jobId = "0";

        TestProbe<APICommand> probe = testKit.createTestProbe();

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.CANCELLING);
        when(persistence.get(jobId)).thenReturn(job);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, mock(JobDetailsPersistence.class), mapper, converter));
        ref.tell(new JobToCancel(jobId, probe.ref()));
        assertEquals(jobId, probe.expectMessageClass(JobStatusInvalid.class).jobId);

        verify(persistence).get(jobId);
    }

    @Test
    public void testStartQueue() throws Exception {
        String jobId = "0";
        String config = "{\"source\": \"empty\", \"tasks\": []}";

        // mocking
        JobPersistence persistence = mock(JobPersistence.class);
        persistence.updateStatus(eq(jobId), eq(JobStatus.RUNNING));
        persistence.updateStatus(eq(jobId), eq(JobStatus.FINISHED));

        JobDetailsPersistence detailsPersistence = mock(JobDetailsPersistence.class);
        JobDetails jobDetails = new JobDetails();
        jobDetails.setId(jobId);
        jobDetails.setSeed("file:///some/path");
        jobDetails.setConfig(new ObjectMapper().readerFor(HashMap.class).readValue(config));
        when(detailsPersistence.getNext()).thenReturn(jobDetails).thenReturn(null);

        ActorRef<APICommand> ref = testKit.spawn(APIQueue.create(1, persistence, detailsPersistence, mapper, converter));
        ref.tell(new StartQueue());

        Thread.sleep(500);

        // verify mock
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.RUNNING));
        verify(persistence).updateStatus(eq(jobId), eq(JobStatus.FINISHED));
        verify(detailsPersistence, times(2)).getNext();
    }

}

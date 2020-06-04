package com.github.euler.api;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;

import com.github.euler.core.JobCommand;
import com.github.euler.core.Sources;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.Behaviors;

public class APIJobExecutionTest extends AkkaTest {

    @Test
    public void testExecution() throws Exception {
        String jobId = "0";
        TestProbe<JobCommand> probe = testKit.createTestProbe();
        APIJob msg = new APIJob(jobId, new URI("file:///some/path"), probe.ref());

        ActorRef<JobCommand> ref = testKit.spawn(APIJobExecution.create(Sources.emptyBehavior(), Behaviors.empty()));
        ref.tell(msg);
        APIJobProcessed resp = probe.expectMessageClass(APIJobProcessed.class);
        assertEquals(jobId, resp.jobId);
    }

}

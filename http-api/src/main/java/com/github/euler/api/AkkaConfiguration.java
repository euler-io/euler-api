package com.github.euler.api;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.euler.api.persistence.JobDetailsPersistence;
import com.github.euler.api.persistence.JobPersistence;
import com.github.euler.configuration.EulerConfigConverter;

import akka.actor.CoordinatedShutdown;
import akka.actor.typed.ActorSystem;

@Configuration
public class AkkaConfiguration {

    private ActorSystem<APICommand> system;

    private final APIConfiguration configuration;
    private final JobPersistence persistence;
    private final JobDetailsPersistence detailsPersistence;
    private final ObjectMapper mapper;

    @Autowired
    public AkkaConfiguration(APIConfiguration configuration, JobPersistence persistence, JobDetailsPersistence detailsPersistence, ObjectMapper mapper) {
        super();
        this.configuration = configuration;
        this.persistence = persistence;
        this.detailsPersistence = detailsPersistence;
        this.mapper = mapper;
    }

    @PostConstruct
    public void postConstruct() {
        start();
    }

    @Bean
    public ActorSystem<APICommand> system() {
        return system;
    }

    private void start() {
        String name = configuration.getConfig().getString("euler.system-name");
        int maxJobs = configuration.getConfig().getInt("euler.queue.max-concurrent-jobs");
        EulerConfigConverter converter = new EulerConfigConverter();
        system = ActorSystem.create(APIQueue.create(maxJobs, persistence, detailsPersistence, mapper, converter), name, configuration.getConfig());
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        stop();
    }

    private void stop() {
        if (system != null) {
            CoordinatedShutdown.get(system).run(CoordinatedShutdown.clusterDowningReason());
        }
    }

    public ActorSystem<APICommand> getSystem() {
        return system;
    }

}

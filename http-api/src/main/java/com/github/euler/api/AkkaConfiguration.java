package com.github.euler.api;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import akka.actor.CoordinatedShutdown;
import akka.actor.typed.ActorSystem;

@Configuration
public class AkkaConfiguration {

    private ActorSystem<APICommand> system;

    private final APIConfiguration configuration;

    public AkkaConfiguration(APIConfiguration configuration) {
        super();
        this.configuration = configuration;
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
//        system = ActorSystem.create(APIQueue.create(), name, configuration.getConfig());
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

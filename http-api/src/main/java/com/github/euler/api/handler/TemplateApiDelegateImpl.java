package com.github.euler.api.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.APICommand;
import com.github.euler.api.JobGenerator;
import com.github.euler.api.JobToEnqueue;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.Template;
import com.github.euler.api.model.TemplateConfig;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.model.TemplateParams;
import com.github.euler.api.persistence.JobDetailsPersistence;
import com.github.euler.api.persistence.TemplatePersistence;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import akka.actor.typed.ActorSystem;

@Service
public class TemplateApiDelegateImpl implements TemplateApiDelegate {

    private final ActorSystem<APICommand> system;
    private final JobDetailsPersistence jobDetailsPersistence;
    private final TemplatePersistence persistence;
    private final TemplateValidator validator;
    private final JobGenerator jobGenerator;

    @Autowired
    public TemplateApiDelegateImpl(ActorSystem<APICommand> system, JobDetailsPersistence jobDetailsPersistence, TemplatePersistence persistence) {
        super();
        this.system = system;
        this.jobDetailsPersistence = jobDetailsPersistence;
        this.persistence = persistence;
        validator = new TemplateValidator();
        jobGenerator = new JobGenerator();
    }

    @Override
    public ResponseEntity<Template> createNewTemplate(TemplateConfig body) {
        if (validator.isValid(body)) {
            try {
                TemplateDetails details = new TemplateDetails();
                Config config = ConfigFactory.parseString(body.getConfig());
                details.setConfig(config.root().render(ConfigRenderOptions.concise()));
                details.setName(body.getName());
                Template template = TemplateUtils.fromDetails(persistence.create(details));
                return new ResponseEntity<Template>(template, HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> deleteTemplate(String templateName) {
        try {
            persistence.delete(templateName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Job> enqueueTemplate(String templateName,
            TemplateParams params) {
        try {
            TemplateDetails template = persistence.get(templateName);
            JobDetails details = jobGenerator.generate(template, params);
            Job job = JobUtils.fromDetails(jobDetailsPersistence.create(details));
            enqueueJob(job.getId());
            return new ResponseEntity<>(job, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void enqueueJob(String jobId) {
        JobToEnqueue msg = new JobToEnqueue(jobId);
        system.tell(msg);
    }

    @Override
    public ResponseEntity<TemplateDetails> getTemplateDetails(String templateName) {
        try {
            TemplateDetails templateDetails = persistence.get(templateName);
            return new ResponseEntity<TemplateDetails>(templateDetails, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

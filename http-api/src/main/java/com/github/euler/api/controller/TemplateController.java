package com.github.euler.api.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.euler.api.APICommand;
import com.github.euler.api.JobGenerator;
import com.github.euler.api.JobToEnqueue;
import com.github.euler.api.JobUtils;
import com.github.euler.api.TemplateUtils;
import com.github.euler.api.TemplateValidator;
import com.github.euler.api.model.Job;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.Template;
import com.github.euler.api.model.TemplateConfig;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.model.TemplateParams;
import com.github.euler.api.persistence.UserJobDetailsPersistence;
import com.github.euler.api.persistence.UserTemplatePersistence;

import akka.actor.typed.ActorSystem;

@RestController
public class TemplateController implements TemplateApi {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final ActorSystem<APICommand> system;
    private final UserJobDetailsPersistence jobDetailsPersistence;
    private final UserTemplatePersistence persistence;

    private final TemplateValidator validator;
    private final JobGenerator jobGenerator;

    @Autowired
    public TemplateController(ActorSystem<APICommand> system, UserJobDetailsPersistence jobDetailsPersistence,
            UserTemplatePersistence persistence) {
        super();
        this.system = system;
        this.jobDetailsPersistence = jobDetailsPersistence;
        this.persistence = persistence;

        this.validator = new TemplateValidator();
        this.jobGenerator = new JobGenerator();
    }

    @Override
    public ResponseEntity<Template> createNewTemplate(TemplateConfig body) {
        if (validator.isValid(body)) {
            try {
                TemplateDetails details = new TemplateDetails();
                details.setConfig(body.getConfig());
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
    public ResponseEntity<Job> enqueueTemplate(String templateName, TemplateParams params) {
        try {
            TemplateDetails template = persistence.get(templateName);
            if (template != null) {
                JobDetails details = jobGenerator.generate(template, params);
                Job job = JobUtils.fromDetails(jobDetailsPersistence.create(details));
                enqueueJob(job.getId());
                return new ResponseEntity<>(job, HttpStatus.OK);
            } else {
                return new ResponseEntity<Job>(new Job(), HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOGGER.warn("Error generating template.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

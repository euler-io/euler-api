package com.github.euler.api.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.euler.api.APICommand;
import com.github.euler.api.JobGenerator;
import com.github.euler.api.model.JobDetails;
import com.github.euler.api.model.Template;
import com.github.euler.api.model.TemplateConfig;
import com.github.euler.api.model.TemplateDetails;
import com.github.euler.api.model.TemplateParams;
import com.github.euler.api.persistence.TemplatePersistence;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import akka.actor.typed.ActorSystem;

@Service
public class TemplateApiDelegateImpl implements TemplateApiDelegate {

    private final ActorSystem<APICommand> system;
    private final TemplatePersistence persistence;
    private final TemplateNameValidator validator;
    private final JobGenerator jobGenerator;

    @Autowired
    public TemplateApiDelegateImpl(ActorSystem<APICommand> system, TemplatePersistence persistence) {
        super();
        this.system = system;
        this.persistence = persistence;
        validator = new TemplateNameValidator();
        jobGenerator = new JobGenerator();
    }

    @Override
    public ResponseEntity<Template> createNewTemplate(TemplateConfig body) {
        if (validator.isValid(body.getName()) && validator.isConfigValid(body.getConfig())) {
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
    public ResponseEntity<Void> enqueueTemplate(TemplateParams params, String templateName) {
        try {
            TemplateDetails template = persistence.get(templateName);
            JobDetails details = jobGenerator.generate(template, params);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
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

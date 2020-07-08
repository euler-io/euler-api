package com.github.euler.api.persistence;

import java.io.IOException;

import com.github.euler.api.model.TemplateDetails;

public interface TemplatePersistence {

    TemplateDetails create(TemplateDetails details) throws IOException;

    void delete(String name) throws IOException;

    TemplateDetails get(String name) throws IOException;

}

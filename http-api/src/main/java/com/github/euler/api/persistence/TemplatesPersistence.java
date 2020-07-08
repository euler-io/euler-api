package com.github.euler.api.persistence;

import java.io.IOException;

import com.github.euler.api.model.TemplateList;

public interface TemplatesPersistence {

    TemplateList list(Integer page, Integer size, String name) throws IOException;

}

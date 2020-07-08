package com.github.euler.api.handler;

import com.github.euler.api.model.Template;
import com.github.euler.api.model.TemplateDetails;

public class TemplateUtils {

    public static Template fromDetails(TemplateDetails details) {
        Template template = new Template();
        template.setName(details.getName());
        return template;
    }
    
}

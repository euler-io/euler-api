package com.github.euler.api.handler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TemplateNameValidatorTest {

    @Test
    public void testNameWithInvalidChars() {
        TemplateNameValidator validator = new TemplateNameValidator();
        assertFalse(validator.isValid("some name"));
        assertFalse(validator.isValid("someName"));
    }

    @Test
    public void testValidNames() {
        TemplateNameValidator validator = new TemplateNameValidator();
        assertTrue(validator.isValid("some-name"));
        assertTrue(validator.isValid("some_name"));
        assertTrue(validator.isValid("some_name1"));
    }

}

package com.github.euler.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.euler.api.TemplateValidator;

public class TemplateNameValidatorTest {

    @Test
    public void testNameWithInvalidChars() {
        TemplateValidator validator = new TemplateValidator();
        assertFalse(validator.isNameValid("some name"));
        assertFalse(validator.isNameValid("someName"));
    }

    @Test
    public void testValidNames() {
        TemplateValidator validator = new TemplateValidator();
        assertTrue(validator.isNameValid("some-name"));
        assertTrue(validator.isNameValid("some_name"));
        assertTrue(validator.isNameValid("some_name1"));
    }

}

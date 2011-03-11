package org.contacts.domain;

import org.contacts.validation.annotation.ValidatorComponent;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@ValidatorComponent
public class ContactValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return Contact.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "Required.firstName", "firstName is required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Required.lastName", "lastName is required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "Required.emailAddress", "emailAddress is required");
  }
}

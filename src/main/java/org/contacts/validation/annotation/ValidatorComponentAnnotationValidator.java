package org.contacts.validation.annotation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidatorComponentAnnotationValidator implements Validator, InitializingBean {
  @Autowired
  private List<Validator> validatorBeans;

  private Collection<Validator> validators = new ArrayList<Validator>();

  public void afterPropertiesSet() throws Exception {
    findAnnotatedValidatorBeans();
  }

  public boolean supports(Class clazz) {
    for (Validator validator : validators) {
      if (validator.supports(clazz)) {
        return true;
      }
    }

    return false;
  }

  public void validate(Object target, Errors errors) {
    for (Validator validator : validators) {
      if (validator.supports(target.getClass())) {
        validator.validate(target, errors);
      }
    }
  }

  private void findAnnotatedValidatorBeans() {
    for (Validator bean : validatorBeans) {
      ValidatorComponent annotation = AnnotationUtils.findAnnotation(bean.getClass(), ValidatorComponent.class);
      if (annotation != null) {
        validators.add(bean);
      }
    }
  }

  public void setValidators(Collection<Validator> validators) {
    this.validators = validators;
  }
}
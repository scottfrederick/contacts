package org.contacts.validation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.Map;

public class TypeMatchingValidator implements Validator, InitializingBean, ApplicationContextAware {
  private ApplicationContext context;
  private Collection<Validator> validators;

  public void afterPropertiesSet() throws Exception {
    findAllValidatorBeans();
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

  private void findAllValidatorBeans() {
    Map<String, Validator> validatorBeans =
            BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Validator.class, true, false);
    validators = validatorBeans.values();
    validators.remove(this);
  }

  public void setApplicationContext(ApplicationContext context) throws BeansException {
    this.context = context;
  }
}
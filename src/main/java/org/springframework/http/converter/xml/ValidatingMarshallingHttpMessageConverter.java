package org.springframework.http.converter.xml;

import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.*;

import javax.xml.transform.Source;

import java.io.IOException;

public class ValidatingMarshallingHttpMessageConverter extends MarshallingHttpMessageConverter {
  private Validator validator;

  @Override
  protected Object readFromSource(Class<Object> clazz, HttpHeaders headers, Source source) throws IOException {
    Object target = super.readFromSource(clazz, headers, source);
    validate(target);
    return target;
  }

  private void validate(Object target) {
    if (validator != null) {
      BindingResult result = getBindingResult(target);
      validator.validate(target, result);
      if (result.hasErrors()) {
        throw new HttpMessageConversionException("Validation of converted object failed", new BindException(result));
      }
    }
  }

  private BindingResult getBindingResult(Object target) {
    return new BeanPropertyBindingResult(target, "target", true);
  }

  public void setValidator(Validator validator) {
    this.validator = validator;
  }
}

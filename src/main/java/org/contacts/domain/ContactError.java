package org.contacts.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactError {
  private String message;

  public ContactError() {
  }

  public ContactError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

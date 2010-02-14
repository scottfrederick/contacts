package org.contacts.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement
public class Contacts {
  private List<Contact> contacts;

  public Contacts() {
    contacts = new ArrayList<Contact>();
  }

  public Contacts(Collection<Contact> contacts) {
    this();
    addContacts(contacts);
  }

  @XmlElement
  public List<Contact> getContact() {
    return contacts;
  }

  public void addContact(Contact contact) {
    contacts.add(contact);
  }

  public void addContacts(Collection<Contact> contacts) {
    this.contacts.addAll(contacts);
  }
}

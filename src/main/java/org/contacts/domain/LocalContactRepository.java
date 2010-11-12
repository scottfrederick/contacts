package org.contacts.domain;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LocalContactRepository implements ContactRepository {
  private Map<Long, Contact> contacts;

  public LocalContactRepository() {
    this.contacts = new HashMap<Long, Contact>();
  }

  public void save(Contact contact) {
    if (contact.getId() == -1) {
      contact.setId(contacts.size());
    }
    contacts.put(contact.getId(), contact);
  }

  public Contact getById(long id) {
    return contacts.get(id);
  }

  public List<Contact> getAll() {
    return new ArrayList<Contact>(contacts.values());
  }

  public void delete(long id) {
    contacts.remove(id);
  }

  public void deleteAll() {
    contacts.clear();
  }
}

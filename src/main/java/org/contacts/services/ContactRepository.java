package org.contacts.services;

import org.contacts.domain.Contact;

import java.util.List;

public interface ContactRepository {
  void save(Contact contact);

  Contact getById(long id);

  List<Contact> getAll();

  void delete(long id);

  void deleteAll();
}

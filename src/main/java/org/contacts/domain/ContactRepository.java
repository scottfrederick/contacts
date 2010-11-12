package org.contacts.domain;

import java.util.List;

public interface ContactRepository {
  void save(Contact contact);

  Contact getById(long id);

  List<Contact> getAll();

  void delete(long id);

  void deleteAll();
}

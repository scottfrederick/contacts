package org.contacts.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class LocalContactRepositoryTest {
  private ContactRepository repository;
  private Contact john;
  private Contact jane;

  @Before
  public void setUp() throws Exception {
    repository = new LocalContactRepository();
    john = new Contact("John", "Smith", "john.smith@test.org");
    jane = new Contact("Jane", "Doe", "jane.doe@test.org");
  }

  @Test
  public void testSaveWillAssignId() throws Exception {
    repository.save(john);
    assertEquals(0, john.getId());

    repository.save(jane);
    assertEquals(1, jane.getId());

    repository.save(john);
    assertEquals(0, john.getId());

    repository.save(jane);
    assertEquals(1, jane.getId());
  }

  @Test
  public void testGetById() throws Exception {
    repository.save(john);
    repository.save(jane);
    assertSame(john, repository.getById(0));
    assertSame(jane, repository.getById(1));
  }

  @Test
  public void testSavedEntriesAreIncludedInGetAll() throws Exception {
    List<Contact> contacts;

    repository.save(john);
    contacts = repository.getAll();
    assertListContains(contacts, john);

    repository.save(john);
    contacts = repository.getAll();
    assertListContains(contacts, john);

    repository.save(jane);
    contacts = repository.getAll();
    assertListContains(contacts, john, jane);
  }

  private void assertListContains(List<Contact> actual, Contact... expected) {
    assertEquals(expected.length, actual.size());
    for (Contact contact : expected) {
      assertTrue(actual.contains(contact));
    }
  }
}

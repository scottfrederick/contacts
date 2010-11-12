package org.contacts.controllers;

import org.contacts.domain.Contact;
import org.contacts.domain.ContactRepository;
import org.contacts.domain.Contacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/contacts")
public class ContactController {
  @Autowired
  protected ContactRepository repository;

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  public Contact addContact(@RequestBody Contact contact) {
    repository.save(contact);
    return contact;
  }

  @RequestMapping(method = RequestMethod.GET)
  public Contacts getAll() {
    return new Contacts(repository.getAll());
  }
}

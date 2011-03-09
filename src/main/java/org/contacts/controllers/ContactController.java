package org.contacts.controllers;

import org.contacts.domain.Contact;
import org.contacts.domain.ContactError;
import org.contacts.domain.ContactRepository;
import org.contacts.domain.Contacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/contacts")
public class ContactController {
  @Autowired
  protected ContactRepository repository;

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  public Contact addContact(@Valid @RequestBody Contact contact) {
    repository.save(contact);
    return contact;
  }

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public Contacts getAll() {
    return new Contacts(repository.getAll());
  }

  @ExceptionHandler({BindException.class, HttpMessageConversionException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleBindException(Exception e) {
    return e.getMessage();
    // return new ContactError(e.getMessage());
  }
}

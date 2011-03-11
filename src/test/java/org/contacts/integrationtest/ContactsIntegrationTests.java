package org.contacts.integrationtest;

import org.contacts.domain.Contact;
import org.contacts.services.ContactRepository;
import org.contacts.test.XPathAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.WebAppContextLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.xpath.XPath;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.contacts.test.XPathAssert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebAppContextLoader.class)
public class ContactsIntegrationTests {
  @Autowired
  protected HttpServlet servlet;

  @Autowired
  protected ContactRepository repository;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private XPath xpath;

  @Before
  public void setUp() {
    xpath = XPathAssert.createXPath();
    repository.deleteAll();
  }

  @Test
  public void servletConfiguredAndInjected() {
    assertNotNull(servlet);
    assertNotNull(repository);
  }

  @Test
  public void createNewContactWillReturnUpdatedInfo() throws Exception {
    initRequestResponse("xml", "POST", "/contacts");
    setRequestContent(buildXmlContent(new Contact("John", "Smith", "john.smith@integrationtest.org")));

    sendRequest();

    assertGoodResponseCode();
    assertResponseContainsElement("/contact/id");
    assertResponseContainsElementWithValue("/contact/firstName", "John");
    assertResponseContainsElementWithValue("/contact/lastName", "Smith");
    assertResponseContainsElementWithValue("/contact/emailAddress", "john.smith@integrationtest.org");
  }

  @Test
  public void createNewContactWillReturnValidationError() throws Exception {
    initRequestResponse("xml", "POST", "/contacts");
    setRequestContent(buildXmlContent(new Contact()));

    sendRequest();

    assertResponseCodeEquals(400);
  }

  @Test
  public void getWillReturnAllCreatedContacts() throws Exception {
    initRequestResponse("xml", "POST", "/contacts");
    setRequestContent(buildXmlContent(new Contact("John", "Smith", "john.smith@integrationtest.org")));
    sendRequest();

    initRequestResponse("xml", "GET", "/contacts");
    sendRequest();

    assertGoodResponseCode();
    assertResponseContainsElement("/contacts");
    assertResponseContainsElement("/contacts/contact[1]");
    assertResponseContainsElement("/contacts/contact[1]/id");
    assertResponseContainsElementWithValue("/contacts/contact[1]/firstName", "John");
    assertResponseContainsElementWithValue("/contacts/contact[1]/lastName", "Smith");
    assertResponseContainsElementWithValue("/contacts/contact[1]/emailAddress", "john.smith@integrationtest.org");

    assertResponseDoesNotContainsElement("/contacts/contact[2]");
  }

  private void initRequestResponse(String contentType, String method, String uri) {
    request = new MockHttpServletRequest(method, uri);
    request.addHeader("Accept", "application/" + contentType);
    request.addHeader("Content-Type", "application/" + contentType);

    response = new MockHttpServletResponse();
  }

  private void setRequestContent(String content) {
    request.setContent(content.getBytes());
  }

  private void sendRequest() throws ServletException, IOException {
    servlet.service(request, response);
  }

  private String buildXmlContent(Contact contact) {
    StringBuilder builder = new StringBuilder();
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append("<contact>");
    if (contact.getFirstName() != null) {
      builder.append(buildXmlElement("firstName", contact.getFirstName()));
    }
    if (contact.getLastName() != null) {
      builder.append(buildXmlElement("lastName", contact.getLastName()));
    }
    if (contact.getEmailAddress() != null) {
      builder.append(buildXmlElement("emailAddress", contact.getEmailAddress()));
    }
    builder.append("</contact>");
    return builder.toString();
  }

  private String buildXmlElement(String element, String value) {
    return new StringBuilder().append("<").append(element).append(">").
            append(value).
            append("</").append(element).append(">").toString();
  }

  private void assertGoodResponseCode() {
    assertResponseCodeEquals(200);
  }

  private void assertResponseCodeEquals(int code) {
    assertEquals(code, response.getStatus());
  }

  private void assertResponseContainsElement(String elementName) throws UnsupportedEncodingException {
    assertNodeExists(xpath, elementName, response.getContentAsString());
  }

  private void assertResponseDoesNotContainsElement(String elementName) throws UnsupportedEncodingException {
    assertNoNodeExists(xpath, elementName, response.getContentAsString());
  }

  private void assertResponseContainsElementWithValue(String elementName, String elementValue)
          throws UnsupportedEncodingException {
    assertNodeEquals(xpath, elementValue, elementName, response.getContentAsString());
  }
}

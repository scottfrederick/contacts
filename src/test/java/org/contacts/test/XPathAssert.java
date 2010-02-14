package org.contacts.test;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.*;

import static junit.framework.Assert.*;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class XPathAssert {
  public static XPath createXPath() {
    return XPathFactory.newInstance().newXPath();
  }

  public static void assertNodeExists(XPath xpath, String expression, String xmlText) {
    Node nodes = evaluateToNode(xpath, xmlText, expression);
    if (nodes == null) {
      fail("Expression " + expression + " not found in " + xmlText);
    }
  }

  public static void assertOneNodeExists(XPath xpath, String expression, String xmlText) {
    NodeList nodes = evaluateToNodeSet(xpath, xmlText, expression);
    if (nodes.getLength() < 1) {
      fail("Expression " + expression + " not found in " + xmlText);
    }
    if (nodes.getLength() > 1) {
      fail("Expression " + expression + " found " + nodes.getLength() + " matches in " +
              xmlText);
    }
  }

  public static void assertNodeCountEquals(XPath xpath, int expectedCount, String expression, String xmlText) {
    int count = countNodes(xpath, xmlText, expression);
    assertEquals(expectedCount, count);
  }

  public static void assertNoNodeExists(XPath xpath, String expression, String xmlText) {
    Node nodes = evaluateToNode(xpath, xmlText, expression);
    if (nodes != null) {
      fail("Unexpected expression " + expression + " found in " + xmlText);
    }
  }

  public static void assertNodeEquals(XPath xpath, String expected, String expression, String xmlText) {
    String actual = evaluateToString(xpath, xmlText, expression);
    if (expected == null) {
      fail("Expression " + expression + " not found in " + xmlText);
    }

    assertEquals("Unequal values for expression: " + expression, expected, actual);
  }

  public static void assertOptionalBooleanNodeEquals(XPath xpath, boolean expected, String expression, String xmlText) {
    String stringValue = expected ? "true" : "";
    assertNodeEquals(xpath, stringValue, expression, xmlText);
  }

  public static void assertNodeContains(XPath xpath, String expected, String expression, String xmlText) {
    String actual = evaluateToString(xpath, xmlText, expression);
    if (actual == null) {
      fail("Expression " + expression + " not found in " + xmlText);
    }

    assertTrue("False value for expression: " + expression, actual.contains(expected));
  }

  private static Node evaluateToNode(XPath xpath, String element, String expression) {
    InputSource input = getInputSource(element);
    return (Node) evaluateXPath(xpath, expression, input, XPathConstants.NODE);
  }

  private static NodeList evaluateToNodeSet(XPath xpath, String xmlText, String expression) {
    InputSource input = getInputSource(xmlText);
    return (NodeList) evaluateXPath(xpath, expression, input, XPathConstants.NODESET);
  }

  private static int countNodes(XPath xpath, String xmlText, String expression) {
    NodeList nodes = evaluateToNodeSet(xpath, xmlText, expression);
    return nodes.getLength();
  }

  private static String evaluateToString(XPath xpath, String xmlText, String expression) {
    InputSource input = getInputSource(xmlText);
    return (String) evaluateXPath(xpath, expression, input, XPathConstants.STRING);
  }

  private static Object evaluateXPath(XPath xpath, String expression, InputSource input, QName nodeType) {
    try {
      return xpath.evaluate(expression, input, nodeType);
    } catch (XPathExpressionException e) {
      throw runtimeException(e);
    }
  }

  private static InputSource getInputSource(String xmlText) {
    return new InputSource(new StringReader(xmlText));
  }

  private static RuntimeException runtimeException(Exception e) {
    throw new RuntimeException(e);
  }

  private static class TestCaseNamespaceContext implements NamespaceContext {
    private Map<String, String> prefixes;

    public TestCaseNamespaceContext() {
      prefixes = new HashMap<String, String>();
    }

    public void addNamespace(String prefix, String uri) {
      prefixes.put(prefix, uri);
    }

    public String getNamespaceURI(String prefix) {
      return prefixes.get(prefix);
    }

    public String getPrefix(String namespaceURI) {
      for (String prefix : prefixes.keySet())
        if (prefixes.get(prefix).equals(namespaceURI))
          return prefix;
      return null;
    }

    public Iterator<String> getPrefixes(String namespaceURI) {
      List<String> list = new ArrayList<String>();
      for (String prefix : prefixes.keySet())
        if (prefixes.get(prefix).equals(namespaceURI))
          list.add(prefix);
      return list.iterator();
    }
  }
}

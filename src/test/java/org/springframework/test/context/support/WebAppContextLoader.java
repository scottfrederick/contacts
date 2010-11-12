package org.springframework.test.context.support;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Map;
import java.util.Map.Entry;

public class WebAppContextLoader extends AbstractContextLoader {
  private String outputDirectory = "/target/classes";
  private String resourceSuffix = "-context.xml";

  public ApplicationContext loadContext(final String... locations) throws Exception {
    GenericWebApplicationContext testWebContext = createWebAppContext();

    loadBeanDefinitions(testWebContext, locations);

    AnnotationConfigUtils.registerAnnotationConfigProcessors(testWebContext);

    testWebContext.refresh();

    DispatcherServlet servlet = initializeServlet(testWebContext);

    return servlet.getWebApplicationContext();
  }

  private GenericWebApplicationContext createWebAppContext() {
    GenericWebApplicationContext webContext = new GenericWebApplicationContext();
    webContext.registerShutdownHook();
    return webContext;
  }

  private ServletContext createServletContext(GenericWebApplicationContext webContext) {
    MockServletContext servletContext = new MockServletContext(outputDirectory, new FileSystemResourceLoader());
    servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webContext);
    webContext.setServletContext(servletContext);
    return servletContext;
  }

  private void loadBeanDefinitions(GenericWebApplicationContext webContext, String[] locations) {
    XmlBeanDefinitionReader beanReader = new XmlBeanDefinitionReader(webContext);
    beanReader.loadBeanDefinitions(locations);
  }

  private DispatcherServlet initializeServlet(GenericWebApplicationContext webContext) throws ServletException {
    Entry<String, DispatcherServlet> servletBean = getDispatcherServletBean(webContext);

    ServletContext servletContext = createServletContext(webContext);

    String servletName = servletBean.getKey();
    DispatcherServlet servlet = servletBean.getValue();

    MockServletConfig servletConfig = new MockServletConfig(servletContext, servletName);
    servlet.init(servletConfig);

    return servlet;
  }

  private Entry<String, DispatcherServlet> getDispatcherServletBean(GenericWebApplicationContext webContext) {
    Map<String, DispatcherServlet> servletBeans = webContext.getBeansOfType(DispatcherServlet.class);

    if (servletBeans.size() == 0) {
      throw new IllegalStateException("No DispatcherServlet was found in the test application context.");
    }

    if (servletBeans.size() > 1) {
      throw new IllegalStateException("Multiple DispatcherServlets were found in the test application context.");
    }

    return servletBeans.entrySet().iterator().next();
  }

  @Override
  protected String getResourceSuffix() {
    return resourceSuffix;
  }

  public void setResourceSuffix(String resourceSuffix) {
    this.resourceSuffix = resourceSuffix;
  }

  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }
}

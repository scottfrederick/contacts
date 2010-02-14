package org.springframework.test.context.support;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
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
  public ConfigurableApplicationContext loadContext(final String... locations) throws Exception {
    GenericWebApplicationContext webContext = createWebAppContext();
    ServletContext servletContext = createServletContext(webContext, "/target/classes");

    loadBeanDefinitions(webContext, locations);

    AnnotationConfigUtils.registerAnnotationConfigProcessors(webContext);

    webContext.refresh();

    initializeServlets(webContext, servletContext);

    return webContext;
  }

  private GenericWebApplicationContext createWebAppContext() {
    GenericWebApplicationContext webContext = new GenericWebApplicationContext();
    webContext.registerShutdownHook();
    return webContext;
  }

  private ServletContext createServletContext(GenericWebApplicationContext webContext, String resourceBasePath) {
    MockServletContext servletContext = new MockServletContext(resourceBasePath, new FileSystemResourceLoader());
    servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webContext);
    webContext.setServletContext(servletContext);
    return servletContext;
  }

  private void loadBeanDefinitions(GenericWebApplicationContext webContext, String[] locations) {
    XmlBeanDefinitionReader beanReader = new XmlBeanDefinitionReader(webContext);
    beanReader.loadBeanDefinitions(locations);
  }

  private void initializeServlets(WebApplicationContext webContext, ServletContext servletContext) throws ServletException {
    Map<String, DispatcherServlet> servlets = webContext.getBeansOfType(DispatcherServlet.class);
    for (Entry<String, DispatcherServlet> entry : servlets.entrySet()) {
      String servletName = entry.getKey();
      DispatcherServlet servlet = entry.getValue();

      MockServletConfig servletConfig = new MockServletConfig(servletContext, servletName);
      servlet.init(servletConfig);
    }
  }

  protected String getResourceSuffix() {
    return "-context.xml";
  }
}
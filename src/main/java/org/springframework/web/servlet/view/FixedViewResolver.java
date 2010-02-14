package org.springframework.web.servlet.view;

import org.springframework.beans.BeansException;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class FixedViewResolver implements ViewResolver, Ordered {
  private int order = Integer.MAX_VALUE;
  private View view;

  public View resolveViewName(String viewName, Locale locale) throws BeansException {
    return view;
  }

  public void setView(View view) {
    this.view = view;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}

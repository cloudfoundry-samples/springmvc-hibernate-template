package org.springsource.cloudfoundry.mvc.web;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
        applicationContext.register(WebMvcConfiguration.class);
        applicationContext.refresh();
    }
}
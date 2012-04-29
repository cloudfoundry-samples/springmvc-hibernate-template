package org.springsource.examples.spring31.web;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springsource.examples.spring31.services.config.ServicesConfiguration;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {

        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        if (cloudEnvironment.isCloudFoundry()) {
            applicationContext.getEnvironment().setActiveProfiles("cloud");
        } else {
            applicationContext.getEnvironment().setActiveProfiles("local");
        }

        Class<?>[] configs = {ServicesConfiguration.class, WebMvcConfiguration.class};

        String[] basePkgs = new String[configs.length];
        int i = 0;
        for (Class<?> pkg : configs)
            basePkgs[i++] = pkg.getPackage().getName();

        applicationContext.scan(basePkgs);
        applicationContext.refresh();
    }
}
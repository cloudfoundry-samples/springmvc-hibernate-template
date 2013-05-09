package org.springsource.cloudfoundry.mvc.web;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

    private CloudEnvironment cloudEnvironment = new CloudEnvironment();

    private boolean isCloudFoundry() {
        return cloudEnvironment.isCloudFoundry();
    }

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {


        String profile;

        if (isCloudFoundry()) {
            profile = "cloud";
        } else {
            profile = "default";
        }

        applicationContext.getEnvironment().setActiveProfiles(profile);

       /* Class<?>[] configs = {ServicesConfiguration.class, WebMvcConfiguration.class};

        String[] basePkgs = new String[configs.length];
        int i = 0;
        for (Class<?> pkg : configs)
            basePkgs[i++] = pkg.getPackage().getName();

        applicationContext.scan(basePkgs);*/
        applicationContext.register( WebMvcConfiguration.class );
        applicationContext.refresh();
    }
}
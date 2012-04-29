package org.springsource.examples.spring31.web;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ApplicationStatusController {

    /**
     * Simple application that wraps the basic
     * information we need to correctly contextualize resources
     */
    static public class Context {
        private String context;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public Context(String context) {
            this.context = context;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/context", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Context context(HttpServletRequest httpServletRequest) {
        return new Context(httpServletRequest.getContextPath());
    }

    @ResponseBody
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CloudEnvironment showCloudEnvironment() {
        return new CloudEnvironment();
    }
}
package org.springsource.cloudfoundry.mvc.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Simple example demonstrating the unique environment properties of your application
 * Obviously, these endpoints shouldn't be included if you go to production!
 *
 * @author Josh Long
 */
@Controller
@Profile("cloud")
public class StatusController {

	@Autowired private Cloud cloud;
	
    @RequestMapping(value = "/properties")
    @ResponseBody
    public Map<String, String> properties() throws Throwable {
        Properties props = System.getProperties();
        Map<String, String> kvs = new HashMap<String, String>();
        for (String k : props.stringPropertyNames())
            kvs.put(k, props.getProperty(k));
        return kvs;
    }

    @RequestMapping(value = "/env")
    @ResponseBody
    public Map<String, String> env() throws Throwable {
        return System.getenv();
    }

    @ResponseBody
    @RequestMapping(value = "/status")
    public Map<String, String> showCloudProperties() {
    		Map<String, String> props = new HashMap<String, String>();
    		
    		for (Entry<Object, Object> entry: cloud.getCloudProperties().entrySet()) {
    			props.put(entry.getKey().toString(), entry.getValue().toString());
    		}
        return props;
    }
}
package org.springsource.examples.spring31.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomerViewController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String customer(HttpServletRequest httpServletRequest, Model model) {
        String ctxPath = httpServletRequest.getContextPath();
        model.addAttribute("context",  ctxPath );
        return "customers";
    }

}

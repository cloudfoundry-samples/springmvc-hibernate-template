package org.springsource.examples.spring31.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springsource.examples.spring31.services.Customer;
import org.springsource.examples.spring31.services.CustomerService;

@RequestMapping("/crm/")
@Controller
public class CustomerApiController {

    @Autowired
    private CustomerService customerService;

    @ResponseBody
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer customerById(@PathVariable("id") Integer id) {
        Customer customer = this.customerService.getCustomerById(id);

        return customer ;
    }

    // http://springmvc31.joshlong.micro/crm/customers
    @ResponseBody
    @RequestMapping(value = "/customers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Customer> customers() {
        return this.customerService.getAllCustomers();
    }

    // http://springmvc31.joshlong.micro/crm/customers
    @RequestMapping(value = "/customers", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addCustomer(@Valid @RequestBody Customer customer) {
        customerService.createCustomer(customer.getFirstName(), customer.getLastName(), customer.getSignupDate());
    }

    @ResponseBody
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.POST ) //,    consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateCustomer(@PathVariable("id") Integer id, @RequestBody Customer customer) {
        customerService.updateCustomer(id, customer.getFirstName(), customer.getLastName(), customer.getSignupDate());
        return id;
    }

/*    @ResponseBody
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
        return id;
    }*/

}             

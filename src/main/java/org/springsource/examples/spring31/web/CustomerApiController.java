package org.springsource.examples.spring31.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springsource.examples.spring31.services.Customer;
import org.springsource.examples.spring31.services.CustomerService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

//
//@RequestMapping(
//        //consumes = MediaType.APPLICATION_JSON_VALUE,
//        produces = MediaType.APPLICATION_JSON_VALUE)
@Controller
public class CustomerApiController {

    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private CustomerService customerService;

    @ResponseBody
    @RequestMapping(value = "/crm/search", method = RequestMethod.GET)
    public Collection<Customer> search(@RequestParam("q") String query) throws Exception {
        Collection<Customer> customers = customerService.search(query);
        if (log.isDebugEnabled())
            log.debug(String.format("retrieved %s results for search query '%s'", Integer.toString(customers.size()), query));
        return customers;
    }

    @ResponseBody
    @RequestMapping(value = "/crm/customer/{id}", method = RequestMethod.GET)
    public Customer customerById(@PathVariable("id") Integer id) {
        return this.customerService.getCustomerById(id);
    }

    // http://springmvc31.joshlong.micro/crm/customers
  /* todo  @ResponseBody
    @RequestMapping(value = "/crm/customers", method = RequestMethod.GET)
    public List<Customer> customers() {
        return this.customerService.getAllCustomers();
    }*/

    // http://springmvc31.joshlong.micro/crm/customers
    @ResponseBody
    @RequestMapping(value = "/crm/customers", method = RequestMethod.PUT)
    public Integer addCustomer(@RequestBody Customer customer) {//@RequestParam("firstName") String fn, @RequestParam("lastName") String ln) {
        String fn = customer.getFirstName(), ln = customer.getLastName();
        return customerService.createCustomer(fn, ln, new Date()).getId();
    }


    @ResponseBody
    @RequestMapping(value = "/crm/customer/{id}", method = RequestMethod.POST)
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

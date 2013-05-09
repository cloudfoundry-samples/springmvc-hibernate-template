package org.springsource.cloudfoundry.mvc.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springsource.cloudfoundry.mvc.services.Customer;
import org.springsource.cloudfoundry.mvc.services.CustomerService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerApiController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired  private CustomerService customerService;

    public static final String CUSTOMERS_ENTRY_URL = "/crm/customers";
    public static final String CUSTOMERS_SEARCH_URL = "/crm/search";
    public static final String CUSTOMERS_BY_ID_ENTRY_URL = CUSTOMERS_ENTRY_URL + "/{id}";

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_SEARCH_URL, method = RequestMethod.GET)
    public Collection<Customer> search(@RequestParam("q") String query) throws Exception {
        Collection<Customer> customers = customerService.search(query);
        if (log.isDebugEnabled())
            log.debug(String.format("retrieved %s results for search query '%s'", Integer.toString(customers.size()), query));
        return customers;
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_BY_ID_ENTRY_URL, method = RequestMethod.GET)
    public Customer customerById(@PathVariable  Integer id) {
        return this.customerService.getCustomerById(id);
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_ENTRY_URL, method = RequestMethod.GET)
    public List<Customer> customers() {
        return this.customerService.getAllCustomers();
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_ENTRY_URL, method = RequestMethod.PUT)
    public Integer addCustomer(@RequestParam String firstName, @RequestParam String lastName) {
        return customerService.createCustomer(firstName, lastName, new Date()).getId();
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_BY_ID_ENTRY_URL, method = RequestMethod.POST)
    public Integer updateCustomer(@PathVariable  Integer id, @RequestBody Customer customer) {
        customerService.updateCustomer(id, customer.getFirstName(), customer.getLastName(), customer.getSignupDate());
        return id;
    }
}
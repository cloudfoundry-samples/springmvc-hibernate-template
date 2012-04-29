package org.springsource.examples.spring31.services;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    static private final String CUSTOMERS_REGION = "customers";

    @Autowired
    private SessionFactory sessionFactory;

    public Customer createCustomer(String firstName, String lastName, Date signupDate) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setSignupDate(signupDate);

        sessionFactory.getCurrentSession().save(customer);
        return customer;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return sessionFactory.getCurrentSession().createCriteria(Customer.class).list();
    }


    @Cacheable(CUSTOMERS_REGION)
    @Transactional(readOnly = true)
    public Customer getCustomerById(Integer id) {
        return (Customer) sessionFactory.getCurrentSession().get(Customer.class, id);
    }

    @CacheEvict(CUSTOMERS_REGION)
    public void deleteCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        sessionFactory.getCurrentSession().delete(customer);
    }

    @CacheEvict( value = CUSTOMERS_REGION  , key = "#id")
    public void updateCustomer(Integer id, String fn, String ln, Date birthday) {
        Customer customer = getCustomerById(id);
        customer.setLastName(ln);
        customer.setSignupDate(birthday);
        customer.setFirstName(fn);
        sessionFactory.getCurrentSession().update(customer);
    }
}

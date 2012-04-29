package org.springsource.examples.spring31.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springsource.examples.spring31.services.config.ServicesConfiguration;

import javax.sql.DataSource;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("local")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class CustomerServiceTest {

    @Configuration
    @Import(ServicesConfiguration.class)
    //  @ComponentScan(basePackageClasses =  CustomerService.class )
    static class CustomerConfiguration {
    }

    @Autowired
    CustomerService customerService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    DataSource dataSource;

    Date signupDate = new Date();

    String firstName = "Josh";

    String lastName = "Long";

    JdbcTemplate jdbcTemplate;

    @Before
    public void before() throws Exception {

        jdbcTemplate = new JdbcTemplate(dataSource);

        TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("delete from CUSTOMER");
            }
        });
    }

    @Test
    public void testCreatingCustomers() {
        Customer customer = customerService.createCustomer(this.firstName, this.lastName, this.signupDate);
        assertNotNull("the customer can't be null", customer);
        assertEquals(customer.getFirstName(), this.firstName);
        assertEquals(customer.getLastName(), this.lastName);
        assertEquals(customer.getSignupDate(), this.signupDate);
    }

    @Test
    public void testUpdatingACustomer() throws Exception {
        Customer customer = customerService.createCustomer(this.firstName, this.lastName, this.signupDate);
        assertNotNull("the customer can't be null", customer);
        assertEquals(customer.getFirstName(), this.firstName);
        assertEquals(customer.getLastName(), this.lastName);
        assertEquals(customer.getSignupDate(), this.signupDate);

        customerService.updateCustomer(customer.getId(), "Joshua", customer.getLastName(), customer.getSignupDate());

        customer = customerService.getCustomerById(customer.getId());
        assertEquals(customer.getFirstName(), "Joshua");

    }
}

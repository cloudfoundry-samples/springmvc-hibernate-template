package org.springsource.cloudfoundry.mvc.services;

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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springsource.cloudfoundry.mvc.services.config.ServicesConfiguration;

import javax.sql.DataSource;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration
public class CustomerServiceTest {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    Date signupDate = new Date();

    String firstName = "Josh";

    String lastName = "Long";

    JdbcTemplate jdbcTemplate;


    @Configuration
    @Import({ServicesConfiguration.class})
    public static class CustomerServiceTestConfiguration {
        // noop we just want the beans in the ServicesConfiguration class
    }


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

    @Test
    public void testSearchingForCustomers() throws Exception {
        Customer customer = customerService.createCustomer(this.firstName, this.lastName, this.signupDate);

        assertEquals(1, customerService.search("josh").size());
    }
}

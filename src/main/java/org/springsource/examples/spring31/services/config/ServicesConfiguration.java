package org.springsource.examples.spring31.services.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springsource.examples.spring31.services.Customer;
import org.springsource.examples.spring31.services.CustomerService;

import java.util.Map;
import java.util.Properties;



@Configuration
@PropertySource("/config.properties")
@EnableCaching
@EnableTransactionManagement
@ComponentScan(basePackageClasses = {CustomerService.class})
public class ServicesConfiguration {

    @Autowired
    private DataSourceConfiguration dataSourceConfiguration;

    @Bean
    @SuppressWarnings("deprecation")
    public SessionFactory sessionFactory() throws Exception {
        Properties props = new Properties();

        Map<String, String> propsMap = this.dataSourceConfiguration.contributeSessionFactoryProperties();

        for (String k : propsMap.keySet())
            props.setProperty(k, propsMap.get(k));

        return new LocalSessionFactoryBuilder(dataSourceConfiguration.dataSource())
                .addAnnotatedClasses(Customer.class)
                .addProperties(props)
                .buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new HibernateTransactionManager(sessionFactory());
    }
}

package org.springsource.cloudfoundry.mvc.services.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springsource.cloudfoundry.mvc.services.CustomerService;


@Configuration
@PropertySource("/config.properties")
@EnableCaching
@EnableTransactionManagement
@Import({CloudFoundryDataSourceConfiguration.class, LocalDataSourceConfiguration.class})
@ComponentScan(basePackageClasses = {CustomerService.class})
public class ServicesConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws Exception {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

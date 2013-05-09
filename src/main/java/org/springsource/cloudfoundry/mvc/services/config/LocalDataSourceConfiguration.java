package org.springsource.cloudfoundry.mvc.services.config;


import org.hibernate.dialect.H2Dialect;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springsource.cloudfoundry.mvc.services.Customer;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("default")
public class LocalDataSourceConfiguration   {


    @Bean
    public DataSource dataSource( Environment environment ) throws Exception {

        return new EmbeddedDatabaseBuilder()
                .setName("crm")
                .setType(EmbeddedDatabaseType.H2)
                .build();

        /*
        String user = environment.getProperty("ds.user"),
                pw = environment.getProperty("ds.password"),
                url = environment.getProperty("ds.url");
        Class<Driver> driverClass = environment.getPropertyAsClass( "ds.driverClass", Driver.class );

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName( driverClass.getName() );
        basicDataSource.setPassword( pw );
        basicDataSource.setUrl( url );
        basicDataSource.setUsername( user );
        basicDataSource.setInitialSize( 5 );
        basicDataSource.setMaxActive( 10 );
        return basicDataSource;
        */

    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean( DataSource dataSource  ) throws Exception {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource( dataSource );
        em.setPackagesToScan(Customer.class.getPackage().getName());
        em.setPersistenceProvider(new HibernatePersistence());
        Map<String, String> p = new HashMap<String, String>();
        p.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        p.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "import_h2.sql");
        p.put(org.hibernate.cfg.Environment.DIALECT, H2Dialect.class.getName());
        p.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");
        em.setJpaPropertyMap(p);
        return em;
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        SimpleCacheManager scm = new SimpleCacheManager();
        Cache cache = new ConcurrentMapCache("customers");
        scm.setCaches(Arrays.asList(cache));
        return scm;
    }

}

package org.springsource.examples.spring31.services.config;

import org.cloudfoundry.runtime.env.*;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.hibernate.dialect.*;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.cache.*;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springsource.examples.spring31.services.Customer;

import javax.sql.DataSource;
import java.util.*;

/**
 * This is a modified version of the original {@link CloudFoundryDataSourceConfiguration} that does <EM>not</EM> require
 * the use of Redis, and that works with <EM>either</EM> PostgreSQL or MySQL.
 *
 * @author Josh Long
 */
@Configuration
@Profile("cloud")
public class CloudFoundryDataSourceConfiguration {

    private CloudEnvironment cloudEnvironment = new CloudEnvironment();

    @Bean
    public RdbmsServiceInfo rdbmsServiceInfo() {
        Collection<RdbmsServiceInfo> serviceInfoList = cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class);
        return serviceInfoList.iterator().next();
    }

    @Bean
    public DataSource dataSource(RdbmsServiceInfo serviceInfo) throws Throwable {
        RdbmsServiceCreator dataSourceCreator = new RdbmsServiceCreator();
        return dataSourceCreator.createService(serviceInfo);
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        SimpleCacheManager scm = new SimpleCacheManager();
        Cache cache = new ConcurrentMapCache("customers");
        scm.setCaches(Arrays.asList(cache));
        return scm;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(RdbmsServiceInfo serviceInfo, DataSource dataSource) throws Throwable {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(Customer.class.getPackage().getName());
        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistence());
        entityManagerFactoryBean.setJpaPropertyMap(buildPropertiesForDataSource(serviceInfo));
        return entityManagerFactoryBean;
    }

    protected Map<String, String> buildPropertiesForDataSource(RdbmsServiceInfo rdbmsServiceCreator) throws Throwable {
        Map<String, String> stringStringHashMap = new HashMap<String, String>();
        stringStringHashMap.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        stringStringHashMap.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");

        String labelGiven =  rdbmsServiceCreator.getLabel();
        System.out.println( "Label: '" + labelGiven+"'");

        String serviceLabel = rdbmsServiceCreator.getLabel().toLowerCase();

        if (serviceLabel.contains("postgre") || serviceLabel.contains("elephantsql")) {
            stringStringHashMap.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "import_psql.sql");
            stringStringHashMap.put(org.hibernate.cfg.Environment.DIALECT, PostgreSQLDialect.class.getName());
        } else if (serviceLabel.contains("mysql") || serviceLabel.contains("cleardb")) {
            stringStringHashMap.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "import_mysql.sql");
            stringStringHashMap.put(org.hibernate.cfg.Environment.DIALECT, MySQL5Dialect.class.getName());
        }
        return stringStringHashMap;
    }
}


package org.springsource.examples.spring31.services.config;


import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("local")
public class LocalDataSourceConfiguration implements DataSourceConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    @SuppressWarnings("unchecked")
    public DataSource dataSource() throws Exception {
        return new EmbeddedDatabaseBuilder()
                .setName("crm")
                .setType(EmbeddedDatabaseType.H2)
                .build();

/*
        // here's how you would do this if you were connecting to a
        // regular DS (perhaps a real PostgreSQL instance or a non in-memory H2 instance)
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();

        Class<? extends Driver> d = (Class<? extends Driver>) Class.forName(environment.getProperty("ds.driverClass"));

        String user = environment.getProperty("ds.user"),
                pw = environment.getProperty("ds.password"),
                url = environment.getProperty("ds.url");

        simpleDriverDataSource.setDriverClass(d);
        simpleDriverDataSource.setUsername(user);
        simpleDriverDataSource.setPassword(pw);
        simpleDriverDataSource.setUrl(url);
        return simpleDriverDataSource;
        */
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        SimpleCacheManager scm = new SimpleCacheManager();
        Cache cache = new ConcurrentMapCache("customers");
        scm.setCaches(Arrays.asList(cache));
        return scm;
    }

    public Map<String, String> contributeSessionFactoryProperties() {
        Map<String, String> p = new HashMap<String, String>();
        p.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        p.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES , "import_h2.sql");
        p.put(org.hibernate.cfg.Environment.DIALECT, H2Dialect.class.getName());
        p.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");
        return p;
    }
}

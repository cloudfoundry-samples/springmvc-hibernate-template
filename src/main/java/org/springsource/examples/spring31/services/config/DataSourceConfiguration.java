package org.springsource.examples.spring31.services.config;

import org.springframework.cache.CacheManager;

import javax.sql.DataSource;
import java.util.Map;

/**
 * This interface extracts the things that change from one environment to another - the
 * properties used to configure a Hibernate 4 {@link org.hibernate.SessionFactory session factory} and
 * a {@link DataSource datasource} - into a separate hierarchy so that implementations may be
 * <em>activated</em> based on which profile is active.
 */
public interface DataSourceConfiguration {
    CacheManager cacheManager() throws Exception;

    DataSource dataSource() throws Exception;

    Map<String, String> contributeSessionFactoryProperties() throws Exception;
}
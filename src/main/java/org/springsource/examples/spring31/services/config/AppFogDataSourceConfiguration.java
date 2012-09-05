package org.springsource.examples.spring31.services.config;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.dialect.PostgreSQLDialect;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of the {@link DataSourceConfiguration dataSourceConfiguration} contract
 * that works with AppFog.
 * <p/>
 * Mainly, this is the same as the {@link CloudFoundryDataSourceConfiguration}, except that
 * it doesn't require Redis, since AppFog doesn't have Redis.
 *
 * @author Josh Long
 */
@Configuration
@Profile("appfog")
public class AppFogDataSourceConfiguration implements DataSourceConfiguration {
    @Bean
    public CacheManager cacheManager() throws Exception {
        SimpleCacheManager scm = new SimpleCacheManager();
        Cache cache = new ConcurrentMapCache("customers");
        scm.setCaches(Arrays.asList(cache));
        return scm;
    }

    private JsonNode fromString(String jsonFragment) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp = factory.createJsonParser(jsonFragment);
        return mapper.readTree(jp);
    }

    @Bean
    public DataSource dataSource() throws Exception {
        String vcapServices = System.getenv("VCAP_SERVICES");
        JsonNode jsonNode = fromString(vcapServices);
        Iterator<JsonNode> pgsqls = jsonNode.get("postgresql-9.1").getElements();
        Assert.isTrue(pgsqls.hasNext(), "there must be at least one postgres instance configured!");
        JsonNode postgresInformation = pgsqls.next();
        Assert.notNull(postgresInformation != null);
        JsonNode credentials = postgresInformation.get("credentials");
        Assert.notNull(credentials, "the credentials can't be null");
        String schemaOrDb = credentials.get("name").getTextValue();
        String host = credentials.get("host").getTextValue();
        int port = credentials.get("port").getNumberValue().intValue();
        String user = credentials.get("user").getTextValue();
        String pw = credentials.get("password").getTextValue();
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        Class<? extends Driver> d = org.postgresql.Driver.class;
        simpleDriverDataSource.setDriverClass(d);
        simpleDriverDataSource.setUsername(user);
        simpleDriverDataSource.setPassword(pw);
        simpleDriverDataSource.setUrl(String.format("jdbc:postgresql://%s:%s/%s", host, port + "", schemaOrDb));
        return simpleDriverDataSource;
    }

    @Override
    public Map<String, String> contributeSessionFactoryProperties() {
        Map<String, String> p = new HashMap<String, String>();
        p.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        p.put(org.hibernate.cfg.Environment.DIALECT, PostgreSQLDialect.class.getName());
        p.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");
        return p;
    }

}

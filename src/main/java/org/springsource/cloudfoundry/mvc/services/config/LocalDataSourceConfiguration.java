package org.springsource.cloudfoundry.mvc.services.config;


import org.hibernate.dialect.H2Dialect;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springsource.cloudfoundry.mvc.services.Customer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class LocalDataSourceConfiguration {

    @Bean
    public DataSource dataSource(Environment environment) throws Exception {
        return new EmbeddedDatabaseBuilder()
                .setName("crm")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource) throws Exception {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
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
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(new JedisConnectionFactory());
        return template;
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        return new RedisCacheManager(redisTemplate());
    }
}

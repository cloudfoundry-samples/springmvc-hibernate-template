package org.springsource.cloudfoundry.mvc.services.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springsource.cloudfoundry.mvc.services.Customer;

@Configuration
@Profile("cloud")
public class CloudFoundryDataSourceConfiguration   {

	@Bean Cloud cloud() {
		CloudFactory cloudFactory = new CloudFactory();
		return cloudFactory.getCloud();
	}

    @Bean
    public DataSource dataSource(Cloud cloud) throws Exception {
    		return cloud.getSingletonServiceConnector(DataSource.class, null);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(Cloud cloud) throws Exception {
        RedisConnectionFactory connectionFactory = cloud.getSingletonServiceConnector(RedisConnectionFactory.class, null);
        RedisTemplate<String, Object> ro = new RedisTemplate<String, Object>();
        ro.setConnectionFactory(connectionFactory);
        return ro;
    }

    
    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean( DataSource dataSource  ) throws Exception {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource( dataSource );
        em.setPackagesToScan(Customer.class.getPackage().getName());
        em.setPersistenceProvider(new HibernatePersistence());
        Map<String, String> p = new HashMap<String, String>();
        p.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        p.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "import_psql.sql");
        p.put(org.hibernate.cfg.Environment.DIALECT, PostgreSQLDialect.class.getName());
        p.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");
        em.setJpaPropertyMap(p);
        return em;
    }

    @Bean
    public CacheManager cacheManager(Cloud cloud) throws Exception {
        return new RedisCacheManager(redisTemplate(cloud));
    }

}


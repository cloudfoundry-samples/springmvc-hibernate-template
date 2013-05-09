package org.springsource.cloudfoundry.mvc.services.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RdbmsServiceInfo;
import org.cloudfoundry.runtime.env.RedisServiceInfo;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.cache.CacheManager;
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

    private CloudEnvironment cloudEnvironment = new CloudEnvironment();

    @Bean
    public DataSource dataSource() throws Exception {
        Collection<RdbmsServiceInfo> mysqlSvc = cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class);
        RdbmsServiceCreator dataSourceCreator = new RdbmsServiceCreator();
        return dataSourceCreator.createService(mysqlSvc.iterator().next());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() throws Exception {
        RedisServiceInfo info = cloudEnvironment.getServiceInfos(RedisServiceInfo.class).iterator().next();
        RedisServiceCreator creator = new RedisServiceCreator();
        RedisConnectionFactory connectionFactory = creator.createService(info);
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
    public CacheManager cacheManager() throws Exception {
        return new RedisCacheManager(redisTemplate());
    }

}


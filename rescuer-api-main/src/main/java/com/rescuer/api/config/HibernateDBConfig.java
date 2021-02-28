package com.rescuer.api.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rescuer.api.entity.User;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "rescueTransactionManager",
        basePackages = { "com.rescuer.api.repository.rescuer" })
public class HibernateDBConfig {

    @Bean("entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Value("${postgres.datasource.url}") String jdbcUrl,
                                                                           @Value("${postgres.datasource.username}") String userName,
                                                                           @Value("${postgres.datasource.password}") String password) throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {

        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.put(Environment.HBM2DDL_AUTO, "update");

        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setPackagesToScan(new String[] {User.class.getPackage().getName() });
        result.setJpaVendorAdapter(jpaVendorAdapter());
        result.setJpaPropertyMap(hibernateProps);
        result.setDataSource(dataSource(jdbcUrl, userName, password));
        return result;
    }

    @Primary
    @Bean("rescueTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    private JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdaptor = new HibernateJpaVendorAdapter();
        jpaVendorAdaptor.setShowSql(true);
        jpaVendorAdaptor.setGenerateDdl(true);
        jpaVendorAdaptor.setDatabasePlatform(PostgreSQL9Dialect.class.getName());
        return jpaVendorAdaptor;
    }

    private DataSource dataSource(String jdbcUrl, String userName, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setMinimumIdle(10);
        dataSource.setMaximumPoolSize(500);
        return dataSource;
    }
}

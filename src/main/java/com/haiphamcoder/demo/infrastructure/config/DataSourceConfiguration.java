package com.haiphamcoder.demo.infrastructure.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.haiphamcoder.demo.shared.DataSourceUtils;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "com.haiphamcoder.demo.adapter.persistence" }, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DataSourceConfiguration {
    @Bean(name = "jpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa")
    public JpaProperties primaryJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("jpaProperties") JpaProperties primaryJpaProperties,
            @Qualifier("dataSource") DataSource primaryDataSource) {
        return DataSourceUtils.createEntityManagerFactoryBean(
                entityManagerFactoryBuilder,
                primaryDataSource,
                primaryJpaProperties,
                "primary",
                "com.haiphamcoder.demo.domain.entity");
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory) {
        return DataSourceUtils.createTransactionManager(primaryEntityManagerFactory.getObject());
    }
}

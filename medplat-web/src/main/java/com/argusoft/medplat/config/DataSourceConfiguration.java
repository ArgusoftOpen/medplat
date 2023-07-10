package com.argusoft.medplat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceConfiguration {

    @Autowired
    DataSource dataSource;


    @Bean(name = "flywayInitializer", initMethod = "migrate")
    public FlywayInitializer flywayInitializer() {
        return new FlywayInitializer();
    }


    @Bean(name = "appSessionFactory")
    @Primary
    @DependsOn("flywayInitializer")
    public LocalSessionFactoryBean getSessionFactory() {            // creating session factory
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan("com.argusoft.medplat");
        sessionFactory.setDataSource(dataSource);
        return sessionFactory;
    }
}

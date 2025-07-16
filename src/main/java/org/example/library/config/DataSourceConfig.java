package org.example.library.config;

import javax.sql.DataSource;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

@Configuration
public class DataSourceConfig {

    // As all databases were handled within the application, a Transaction Manager,
    // Jdbc Template, Entity Manager Factory and DataSource need to be defined so that
    // each database can be correctly addressed.

    // Method for handling data connector for Operational Database
    @Bean
    @Qualifier("operationalTransactionManager")
    PlatformTransactionManager operationalTransactionManager(@Qualifier("operationalDataSource") DataSource operationalDataSource) {
        return new DataSourceTransactionManager(operationalDataSource);
    }

    @Bean
    @Qualifier("operationalDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.operational")
    public DataSource operationalDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("operationalJdbcTemplate")
    JdbcTemplate operationalJdbcTemplate(@Qualifier("operationalDataSource") DataSource operationalDataSource) {
        return new JdbcTemplate(operationalDataSource);
    }

    @Bean
    @Qualifier("operationalEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean operationalEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("operationalDataSource") DataSource operationalDataSource) {
        Map<String, String> properties = new HashMap<>();
        return builder.dataSource(operationalDataSource)
                .packages("org.example.library.model.operational")
                .properties(properties)
                .persistenceUnit("operational")
                .build();
    }

    // Method for handling data connector for Staging Database
    @Bean
    @Qualifier("stagingTransactionManager")
    PlatformTransactionManager stagingTransactionManager(@Qualifier("stagingDataSource") DataSource stagingDataSource) {
        return new DataSourceTransactionManager(stagingDataSource);
    }

    @Bean
    @Qualifier("stagingDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.staging")
    public DataSource stagingDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("stagingJdbcTemplate")
    JdbcTemplate stagingJdbcTemplate(@Qualifier("stagingDataSource") DataSource stagingDataSource) {
        return new JdbcTemplate(stagingDataSource);
    }

    @Bean
    @Qualifier("stagingEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean stagingEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("stagingDataSource") DataSource stagingDataSource) {
        Map<String, String> properties = new HashMap<>();
        return builder.dataSource(stagingDataSource)
                .packages("org.example.library.model.staging")
                .properties(properties)
                .persistenceUnit("staging")
                .build();
    }


    // Method for handling data connector for Warehouse Database
    @Bean
    @Qualifier("warehouseTransactionManager")
    PlatformTransactionManager warehouseTransactionManager(@Qualifier("warehouseDataSource") DataSource warehouseDataSource) {
        return new DataSourceTransactionManager(warehouseDataSource);
    }

    @Bean
    @Qualifier("warehouseDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.warehouse")
    public DataSource warehouseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("warehouseJdbcTemplate")
    JdbcTemplate warehouseJdbcTemplate(@Qualifier("warehouseDataSource") DataSource warehouseDataSource) {
        return new JdbcTemplate(warehouseDataSource);
    }

    @Bean
    @Qualifier("warehouseEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean warehouseEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                         @Qualifier("warehouseDataSource") DataSource warehouseDataSource) {
        Map<String, String> properties = new HashMap<>();
        return builder.dataSource(warehouseDataSource)
                .packages("org.example.library.model.warehouse")
                .properties(properties)
                .persistenceUnit("warehouse")
                .build();
    }
}

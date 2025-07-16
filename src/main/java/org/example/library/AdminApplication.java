package org.example.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
    @Configuration
    public static class AppConfig {

        // The below were added to help with some stability as occasionally the app would dump on startup without it.
        // Define default Transactionm Manager
        @Bean(name = "transactionManager")
        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }

        // Define a default Entity Manager
        @Bean(name = "entityManagerFactory")
        @Primary
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                           DataSource dataSource) {
            Map<String, String> properties = new HashMap<>();
            return builder.dataSource(dataSource)
                    .packages("org.example.library.model") // Specify base package to scan for entities
                    .properties(properties)
                    .build();
        }
    }
}

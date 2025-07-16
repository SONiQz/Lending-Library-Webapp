package org.example.library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import org.example.library.config.DataSourceConfig;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
public class DataSourceTest {

    @Autowired
    private DataSourceConfig dataSourceConfig;
    @Qualifier("warehouseDataSource")
    @Autowired
    private DataSource warehouseDataSource;
    @Qualifier("warehouseJdbcTemplate")
    @Autowired
    private JdbcTemplate warehouseJdbcTemplate;

    @Test
    public void testConnection() {
        try (Connection conn = DataSourceUtils.getConnection(warehouseDataSource)){
            System.out.println("Successfully connected to the database.");
            // Optional: perform a simple query test
            JdbcTemplate jdbcTemplate = new JdbcTemplate(warehouseDataSource);
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BOOKS", Integer.class);
            System.out.println("Number of entries in BOOKS: " + count);
        } catch (SQLException ex) {
            System.err.println("Failed to connect to the database.");
            ex.printStackTrace();
        }
    }
}

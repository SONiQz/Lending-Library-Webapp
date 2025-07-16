package org.example.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
public class warehouseDailyStatsRepositoryImpl implements warehouseDailyStatsRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    // Making sure it's looking at the Warehouse Database
    @Autowired
    public warehouseDailyStatsRepositoryImpl(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // PL/SQL to get sum of new users (i.e. from daily stats)
    @Override
    public long sumUsersByDate(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(b.NO_OF_USERS) FROM DAILY_STATS b " +
                "WHERE STAT_DATE BETWEEN ? AND ?";

        long result = jdbcTemplate.queryForObject(sql, new Object[]{startDate, endDate}, Long.class);
        return result;
    }

    // PL/SQL to get total users from Users table
    public long sumUsers(){
        String sql = "SELECT COUNT(*) FROM USERS";
        long result = jdbcTemplate.queryForObject(sql, Long.class);
        return result;
    }

    // Get Sum of No. of Books added for date range from Daily_stats
    public int addedBooks(LocalDate startDate, LocalDate endDate){
        String sql = "SELECT SUM(NO_OF_BOOKS) FROM DAILY_STATS " +
                "WHERE STAT_DATE BETWEEN ? AND ?";
        int result = jdbcTemplate.queryForObject(sql, new Object[]{startDate, endDate}, Integer.class);
        return result;
    }
}

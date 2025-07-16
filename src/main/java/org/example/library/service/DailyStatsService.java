package org.example.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DailyStatsService {

    private static final Logger logger = LoggerFactory.getLogger(DailyStatsService.class);

    // Make sure it's using the Staging Database
    @Autowired
    @Qualifier("stagingJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    // Use Staging Transaction Manager - Which would potentially allow for the rolling back of commits
    @Transactional("stagingTransactionManager")
    // Creating the daily stats table data
    public void saveDailyStats() {
        try {
            LocalDate today = LocalDate.now();

            logger.info("Saving daily stats for {}", today);

            // Fetch statistics from the database
            int totalBooks = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BOOKS", Integer.class);
            int totalUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USERS", Integer.class);
            int totalLoans = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM LOANS", Integer.class);
            int activeLoansCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM LOANS WHERE DATE_RETURNED IS NULL", Integer.class);

            // Logging to see the operation in action for debugging
            logger.debug("Total books: {}", totalBooks);
            logger.debug("Total users: {}", totalUsers);
            logger.debug("Total loans: {}", totalLoans);
            logger.debug("Active loans count: {}", activeLoansCount);

            // Insert the new daily stats record
            String insertSql = "INSERT INTO DAILY_STATS (STAT_DATE, NO_OF_BOOKS, NO_OF_USERS, CURRENT_LOANS) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, today, totalBooks, totalUsers, activeLoansCount);

            logger.info("Daily stats saved successfully for {}", today);
        } catch (Exception e) {
            logger.error("Failed to save daily stats: {}", e.getMessage(), e);
        }
    }
}

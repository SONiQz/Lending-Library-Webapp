package org.example.library.repository;

import org.example.library.dto.userLoanFrequencyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class warehouseLoanRepositoryImpl implements warehouseLoanRepositoryCustom {
    
    private static final Logger logger = LoggerFactory.getLogger(warehouseLoanRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    // Still making sure it's looking at the correct DB
    @Autowired
    public warehouseLoanRepositoryImpl(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // PL/SQL to get average frequency of loans i.e. count all loans (x) and distinct barcodes (y) and simply x/y
    @Override
    public float avgLoanFrequency() {
        String sql = "SELECT COUNT(*) / COUNT(DISTINCT barcode) FROM LOANS";
        float result = jdbcTemplate.queryForObject(sql, float.class);
        return result;
    }

    // PL/SQL to select date returned from date loaned to return a positive double
    // then calculate the average across the table to establish the average loan in days.
    @Override
    public double avgLoanDuration(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT AVG(DATE_RETURNED - DATE_LOANED) " +
            "FROM LOANS " +
            "WHERE DATE_LOANED BETWEEN ? AND ?" +
            "AND DATE_RETURNED > DATE_LOANED " +
            "AND DATE_LOANED IS NOT NULL " +
            "AND DATE_RETURNED IS NOT NULL";

        double result = jdbcTemplate.queryForObject(sql, Double.class, startDate, endDate);
        return result;
    }

    // Caclulate the sum of fees from the loans table for a given date range
    public float feesForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(FEES) " +
                "FROM LOANS " +
                "WHERE DATE_LOANED BETWEEN ? AND ?";
        float result = jdbcTemplate.queryForObject(sql, float.class, startDate, endDate);
        return result;
    }

    // Using the userLoanFrequencyDTO to manage the return joined table data for users with the most
    // loans and crop to first 5 highest values.
    @Override
    public List<userLoanFrequencyDTO> mostFrequentUsers(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(l.USER_ID) as frequency, l.USER_ID, u.FIRST_NAME, u.LAST_NAME " +
                "FROM LOANS l " +
                "LEFT JOIN USERS u ON u.BARCODE = l.USER_ID " +
                "WHERE l.DATE_LOANED BETWEEN ? AND ? " +
                "GROUP BY l.USER_ID, u.FIRST_NAME, u.LAST_NAME " +
                "ORDER BY frequency DESC " +
                "FETCH FIRST 5 ROWS ONLY";

        return jdbcTemplate.query(sql, new Object[]{startDate, endDate}, (rs, rowNum) -> {
            String id = rs.getString("USER_ID");
            String firstName = rs.getString("FIRST_NAME");
            String lastName = rs.getString("LAST_NAME");
            int frequency = rs.getInt("frequency");
            return new userLoanFrequencyDTO(id, firstName, lastName, frequency);
        });
    }
}

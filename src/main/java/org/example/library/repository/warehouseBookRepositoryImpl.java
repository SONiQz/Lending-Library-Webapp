package org.example.library.repository;

import org.example.library.dto.BookStats;
import org.example.library.model.warehouse.warehouseBookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class warehouseBookRepositoryImpl implements warehouseBookRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    // Ensuring it uses the warehouseJdbcTemplate connector (i.e. the Data Warehouse database)
    @Autowired
    public warehouseBookRepositoryImpl(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // PL/SQL for getting 10 Top Books
    @Override
    public List<BookStats> findTopBooks() {
        String sql = "SELECT COUNT(l.barcode) AS count, b.barcode, b.title, b.author " +
                "FROM LOANS l JOIN BOOKS b ON b.barcode = l.barcode " +
                "GROUP BY b.barcode, b.title, b.author ORDER BY COUNT(l.barcode) DESC, b.title DESC " +
                "FETCH FIRST 10 ROWS ONLY";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long count = rs.getLong("count");
            String barcode = rs.getString("barcode");
            String title = rs.getString("title");
            String author = rs.getString("author");
            return new BookStats(count, barcode, title, author);
        });
    }

    // PL/SQL get Count of all books
    @Override
    public long countBooks() {
        String sql = "SELECT COUNT(b.barcode) AS count FROM BOOKS b";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }


    // Date Ranged Queries
    //PL/SQL for Books Edited by Date Range (Using Timestamp)
    @Override
    public List<warehouseBookModel> booksEditedByRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT barcode, title, author, timestamp FROM BOOKS WHERE timestamp BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{startDate, endDate}, (rs, rowNum) -> {
            String barcode = rs.getString("barcode");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String timestamp = rs.getString("timestamp");
            return new warehouseBookModel(barcode, title, author, timestamp);
        });
    }

    // PL/SQL to get the Count of book records created for date range from the Daily Stats table
    @Override
    public long countBooksByRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(NO_OF_BOOKS) FROM DAILY_STATS WHERE STAT_DATE BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{startDate, endDate}, Long.class);
    }
}

package org.example.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.sql.Timestamp;

@Service
public class DataCopyService {

    private static final Logger logger = LoggerFactory.getLogger(DataCopyService.class);

    @Autowired
    @Qualifier("operationalJdbcTemplate")
    private JdbcTemplate operationalJdbcTemplate;

    @Autowired
    @Qualifier("stagingJdbcTemplate")
    private JdbcTemplate stagingJdbcTemplate;

    @Autowired
    @Qualifier("warehouseJdbcTemplate")
    private JdbcTemplate warehouseJdbcTemplate;

    @Autowired
    @Qualifier("operationalDataSource")
    private DataSource operationalDataSource;

    @Autowired
    @Qualifier("stagingDataSource")
    private DataSource stagingDataSource;

    @Autowired
    @Qualifier("warehouseDataSource")
    private DataSource warehouseDataSource;

    public void logDataSourceDetails() {
        logDetails(operationalDataSource, "Operational Database");
        logDetails(stagingDataSource, "Staging Database");
        logDetails(warehouseDataSource, "Warehouse Database");
    }

    private void logDetails(DataSource dataSource, String description) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            logger.info("{} - JDBC URL: {}", description, metaData.getURL());
            logger.info("{} - User Name: {}", description, metaData.getUserName());
        } catch (SQLException e) {
            logger.error("Error accessing data source details for {}: {}", description, e.getMessage(), e);
        }
    }

    @Autowired
    private DailyStatsService dailyStatsService;

    @PostConstruct
    public void initializeXferTable() {
        logger.info("Initializing LastXfer table with starting values...");
        initializeXfers("USERS");
        initializeXfers("LOANS");
        initializeXfers("BOOKS");
        logDataSourceDetails();
    }

    // Generate default LastXfers data if it's not populated to make sure it's able to run (cos no dates means no worK)
    private void initializeXfers(String tableName) {
        logger.info("Initializing LastXfer table for table: {}", tableName);
        String mergeSql = "MERGE INTO LAST_XFER USING DUAL ON (TABLE_NAME = ?) " +
                "WHEN NOT MATCHED THEN INSERT (TABLE_NAME, LAST_DATE) VALUES (?, TO_TIMESTAMP('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS.FF9'))";

        stagingJdbcTemplate.update(mergeSql, tableName, tableName);
        logger.info("LastXfer table initialized for table: {}", tableName);
    }

    // Schedule the task to run at 1AM
    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/London")
    public void copyDataFromOperationalToStaging() {
        logger.info("Copying data from operational to staging...");

        Map<String, Timestamp> lastProcessedTimestamps = getLastProcessedTimestampsFromLastXfer();

        for (Map.Entry<String, Timestamp> entry : lastProcessedTimestamps.entrySet()) {
            String tableName = entry.getKey();
            Timestamp lastProcessedTimestamp = entry.getValue();

            logger.info("Processing table: {}, Last Processed Timestamp: {}", tableName, lastProcessedTimestamp);

            // Truncate staging tables
            String truncateSql = "TRUNCATE TABLE " + tableName;
            try {
                stagingJdbcTemplate.update(truncateSql);
                logger.info("Staging table {} truncated successfully", tableName);
            } catch (DataAccessException e) {
                logger.error("Error truncating staging table {}: {}", tableName, e.getMessage());
                e.printStackTrace();
            }

            // Gather rows where timestamp is newer than LastXfer
            String query = "SELECT * FROM " + tableName + " WHERE TIMESTAMP > ?";
            logger.info("Generated SQL query: {}", query);

            List<Map<String, Object>> rows = operationalJdbcTemplate.queryForList(query, lastProcessedTimestamp);

            logger.info("Copying data to staging database for table: {}", tableName);
            // Create map of the rows to make it easier to insert.
            for (Map<String, Object> row : rows) {
                Timestamp timestamp = (Timestamp) row.get("TIMESTAMP");

                if (timestamp.after(lastProcessedTimestamp)) {
                    logger.info("Row values: {}", row);
                    Object[] queryAndParams = buildInsertQuery(tableName, row);
                    logger.info("Executing query: {}", queryAndParams[0]);
                    logger.info("With parameters: {}", queryAndParams[1]);
                    stagingJdbcTemplate.update((String) queryAndParams[0], (Object[]) queryAndParams[1]);
                    logger.info("Row copied successfully for table: {}", tableName);
                } else {
                    logger.info("Skipping row for {} table as timestamp is not after the last processed timestamp.", tableName);
                }
            }

            Timestamp lastTimestampFromRows = getLastTimestampFromRows(rows);
            logger.info("Last Timestamp from Rows: {}", lastTimestampFromRows);

            updateLastProcessedTimestampInLastXfer(tableName, lastTimestampFromRows);
        }
        logger.info("Completed copying operational to staging...");
        dailyStatsService.saveDailyStats();
    }

    // Construction of the Merge queries to allow the insert/update/replacement of data in the warehouse table
    private Object[] buildMergeSql(String tableName, Map<String, Object> row) {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("MERGE INTO " + tableName + " USING DUAL ON (ID = ?) ");
        parameters.add(row.get("ID")); // Assuming ID is the primary key for matching

        // Start building the UPDATE part
        StringBuilder updatePart = new StringBuilder("WHEN MATCHED THEN UPDATE SET ");
        boolean isFirst = true; // Flag to manage commas
        for (String key : row.keySet()) {
            if (!"ID".equals(key)) { // Skip ID for update part
                if (!isFirst) {
                    updatePart.append(", ");
                }
                updatePart.append(key + " = ?");
                parameters.add(row.get(key)); // Add update parameter
                isFirst = false;
            }
        }

        // Start building the INSERT part
        StringBuilder insertColumns = new StringBuilder("WHEN NOT MATCHED THEN INSERT (");
        StringBuilder insertValues = new StringBuilder("VALUES (");
        isFirst = true; // Reset flag for insert part
        for (String key : row.keySet()) {
            if (!isFirst) {
                insertColumns.append(", ");
                insertValues.append(", ");
            }
            insertColumns.append(key);
            insertValues.append("?");
            parameters.add(row.get(key)); // Add insert parameter
            isFirst = false;
        }
        insertColumns.append(") ");
        insertValues.append(")");

        // Combine all parts
        sql.append(updatePart);
        sql.append(insertColumns);
        sql.append(insertValues);

        return new Object[]{sql.toString(), parameters.toArray()};
    }

    // SQL Operation to copy data from staging to data warehouse
    public void copyDataFromStagingToWarehouse() {
        logger.info("Starting data transfer from staging to warehouse...");
        String[] tableNames = {"books", "loans", "users","daily_stats"};

        for (String tableName : tableNames) {
            logger.info("Processing table: {}", tableName);
            String selectQuery = "SELECT * FROM " + tableName;
            List<Map<String, Object>> rows = stagingJdbcTemplate.queryForList(selectQuery);

            for (Map<String, Object> row : rows) {
                try {
                    Object[] sqlAndParams = buildMergeSql(tableName, row);
                    String mergeSql = (String) sqlAndParams[0];
                    Object[] params = (Object[]) sqlAndParams[1];
                    int result = warehouseJdbcTemplate.update(mergeSql, params);
                    logger.info("Rows affected: {}", result);
                } catch (Exception e) {
                    logger.error("Error replacing data for table: {} with data: {}. Error: {}", tableName, row, e.getMessage(), e);
                }
            }
        }
        logger.info("Data transfer to warehouse completed.");
    }

    // get latest timestamp from each table to make sure there is work to do
    private Timestamp getLastTimestampFromRows(List<Map<String, Object>> rows) {
        Timestamp lastTimestamp = null;

        for (Map<String, Object> row : rows) {
            Timestamp timestamp = (Timestamp) row.get("TIMESTAMP");
            if (lastTimestamp == null || timestamp.after(lastTimestamp)) {
                lastTimestamp = timestamp;
            }
        }

        return lastTimestamp;
    }
    // Get the last Timestamp to validate if there is work to do
    private Map<String, Timestamp> getLastProcessedTimestampsFromLastXfer() {
        logger.info("Fetching last processed timestamps from LastXfer table...");
        List<Map<String, Object>> rows = stagingJdbcTemplate.queryForList("SELECT * FROM LAST_XFER");
        Map<String, Timestamp> lastProcessedTimestamps = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String tableName = (String) row.get("TABLE_NAME");
            Timestamp lastTimestamp;
            Object lastDateObj = row.get("LAST_DATE");
            if (lastDateObj != null) {
                lastTimestamp = (Timestamp) lastDateObj;
            } else {
                // Set default timestamp to '1970-01-01 00:00:00' As without it it will fail
                lastTimestamp = Timestamp.valueOf("1970-01-01 00:00:00");
            }
            lastProcessedTimestamps.put(tableName, lastTimestamp);
        }
        logger.info("Last processed timestamps fetched from LastXfer table: {}", lastProcessedTimestamps);
        return lastProcessedTimestamps;
    }

    // update LastXfer timestamp dates
    private void updateLastProcessedTimestampInLastXfer(String tableName, Timestamp lastProcessedTimestamp) {
        Timestamp currentLastProcessedTimestamp = getLastProcessedTimestampFromLastXfer(tableName);

        if (lastProcessedTimestamp == null) {
            logger.warn("Last processed timestamp for table {} is null. Skipping update.", tableName);
            return;
        }

        // Handle the case where currentLastProcessedTimestamp is null
        else if (currentLastProcessedTimestamp == null || lastProcessedTimestamp.after(currentLastProcessedTimestamp)) {
            logger.info("Updating LAST_XFER table for table {}: new lastProcessedTimestamp = {}", tableName, lastProcessedTimestamp);
            int rowsAffected = stagingJdbcTemplate.update("UPDATE LAST_XFER SET LAST_DATE = ? WHERE TABLE_NAME = ?", lastProcessedTimestamp, tableName);
            logger.info("Rows affected in LAST_XFER table update for table {}: {}", tableName, rowsAffected);
        } else {
            logger.info("Skipping LAST_XFER table update for table {}: new lastProcessedTimestamp = {} is not after current value {}", tableName, lastProcessedTimestamp, currentLastProcessedTimestamp);
        }
    }

    // A fun Error checking function to validate the Last Timestamp for each table from LastXfer table
    private Timestamp getLastProcessedTimestampFromLastXfer(String tableName) {
        try {
            return stagingJdbcTemplate.queryForObject(
                    "SELECT LAST_DATE FROM LAST_XFER WHERE TABLE_NAME = ?", Timestamp.class, tableName);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching last processed timestamp from LAST_XFER table for table {}: {}", tableName, e.getMessage());
            return null;
        }
    }

    private Object[] buildInsertQuery(String tableName, Map<String, Object> values) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder valuesBuilder = new StringBuilder(") VALUES (");

        List<Object> parameters = new ArrayList<>();

        // Iterate over the keys in the values map to build the column names and parameter values
        boolean first = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (!first) {
                queryBuilder.append(", ");
                valuesBuilder.append(", ");
            }
            String column = entry.getKey();
            Object value = entry.getValue();
            queryBuilder.append(column);
            valuesBuilder.append("?");
            parameters.add(value);
            first = false;
        }

        // Complete the query string
        queryBuilder.append(valuesBuilder).append(")");

        // Log the generated SQL statement to the debug log
        String insertQuery = queryBuilder.toString();
        logger.info("Generated INSERT SQL query: {}", insertQuery);

        return new Object[]{insertQuery, parameters.toArray()};
    }
}

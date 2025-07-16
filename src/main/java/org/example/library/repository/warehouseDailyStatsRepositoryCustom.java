package org.example.library.repository;
import java.time.LocalDate;

// Definition of the parameters of DailyStats Repo calls
public interface warehouseDailyStatsRepositoryCustom {
    // Long with Date Params
    long sumUsersByDate(LocalDate startDate, LocalDate endDate);
    // Long
    long sumUsers();
    // Int with Start and End date params
    int addedBooks(LocalDate startDate, LocalDate endDate);
}

package org.example.library.repository;

import org.example.library.dto.userLoanFrequencyDTO;

import java.time.LocalDate;
import java.util.List;

public interface warehouseLoanRepositoryCustom {

    // Float
    float avgLoanFrequency();

    // Double with Date Params
    double avgLoanDuration(LocalDate startDate, LocalDate endDate);

    // Float with Date Params
    float feesForDateRange(LocalDate startDate, LocalDate endDate);

    // List of users with date range.
    List<userLoanFrequencyDTO> mostFrequentUsers(LocalDate startDate, LocalDate endDate);
}

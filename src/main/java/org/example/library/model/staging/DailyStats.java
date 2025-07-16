package org.example.library.model.staging;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDate;

// Model for the Daily_Stats Table so that Setters and Getters could be used to handle data as needed.

@Entity
@Table(name = "DAILY_STATS")
public class DailyStats {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-gen")
    @GenericGenerator(
            name = "sequence-gen",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "DAILY_STATS_SEQUENCE"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private LocalDate statDate;
    private Long noOfBooks;
    private Long noOfUsers;
    private Number currentLoans;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStatDate() {
        return statDate;
    }

    public void setStatDate(LocalDate date) {
        this.statDate = date;
    }

    public Long getNoOfBooks() {
        return noOfBooks;
    }

    public void setNoOfBooks(Long noOfBooks) {
        this.noOfBooks = noOfBooks;
    }

    public Long getNoOfUsers() {
        return noOfUsers;
    }

    public void setNoOfUsers(Long noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public Number getFeesPaid() {
        return currentLoans;
    }

    public void setFeesPaid(Number feesPaid) {
        this.currentLoans = feesPaid;
    }
}

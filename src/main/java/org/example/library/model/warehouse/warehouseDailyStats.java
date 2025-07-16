package org.example.library.model.warehouse;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDate;

@Entity
//Define Daily_stats table model
@Table(name = "Daily_Stats")
public class warehouseDailyStats {
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
    @Column(name = "ID")
    private Long id;
    @Column(name = "STAT_DATE")
    private LocalDate statDate;
    @Column(name = "NO_OF_BOOKS")
    private Long noOfBooks;
    @Column(name = "NO_OF_USERS")
    private Long noOfUsers;
    @Column(name = "CURRENT_LOANS")
    private Number currentLoans;

    private Long sumOfUsers;

    public warehouseDailyStats(Long sumOfUsers) {
        this.sumOfUsers = sumOfUsers;
    }

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

    public Number getCurrentLoans() {
        return currentLoans;
    }

    public void setCurrentLoans(Number currentLoans) {
        this.currentLoans = currentLoans;
    }

    public Long getSumOfUsers() {
        return sumOfUsers;
    }

    public void setSumOfUsers(Long sumOfUsers) {
        this.sumOfUsers = sumOfUsers;
    }
}

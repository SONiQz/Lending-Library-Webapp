package org.example.library.model.warehouse;

import jakarta.persistence.*;

import java.time.LocalDate;

// Loan Model
@Entity
// Define Table name for "Loans"
@Table(name = "Loans")
// define table model
public class warehouseLoanModel {
    // Define ID (to suppress the creation of automated _ID with calculated value)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "USER_ID")
    private String userId;
    private String barcode;
    @Column(name = "DATE_LOANED")
    private LocalDate dateLoaned;
    @Column(name = "DUE_DATE")
    private LocalDate dueDate;
    @Column(name = "DATE_RETURNED")
    private LocalDate dateReturned;
    private double fees;
    @Column(name = "FEES_PAID")
    private boolean feesPaid;
    @Column(name = "FEES_PAID_DATE")
    private LocalDate feesPaidDate;


    // Default constructor
    public warehouseLoanModel() {
    }


    // Constructor with parameters
    public warehouseLoanModel(String barcode, LocalDate dateLoaned, LocalDate dateReturned, LocalDate dueDate, double fees, String userId, boolean feesPaid, LocalDate feesPaidDate) {
        this.userId = userId;
        this.barcode = barcode;
        this.dateLoaned = dateLoaned;
        this.dateReturned = dateReturned;
        this.dueDate = dueDate;
        this.fees = fees;
        this.feesPaid = feesPaid;
        this.feesPaidDate = feesPaidDate;
    }

    // Getters and setters for all fields

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public LocalDate getDateLoaned() {
        return dateLoaned;
    }

    public void setDateLoaned(LocalDate dateLoaned) {
        this.dateLoaned = dateLoaned;
    }

    public LocalDate getDateDue() {
        return dueDate;
    }

    public void setDateDue(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDateReturned(LocalDate dateReturned) {
        this.dateReturned = dateReturned;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public Object getDateReturned() {
        return this.dateReturned;
    }

    public void setFeesPaid(boolean feesPaid){ this.feesPaid = feesPaid; }

    public void setFeesPaidDate(LocalDate feesPaidDate){ this.feesPaidDate = feesPaidDate; }
    public LocalDate getFeesPaidDate() {
        return feesPaidDate;
    }
}

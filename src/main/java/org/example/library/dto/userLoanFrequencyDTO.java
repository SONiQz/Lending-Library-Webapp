package org.example.library.dto;

// A DTO to handle data from the loans and users tables so it could be more easily tabulated.

public class userLoanFrequencyDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private int frequency;

    // Constructor
    public userLoanFrequencyDTO(String userId, String firstName, String lastName, int frequency) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.frequency = frequency;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}

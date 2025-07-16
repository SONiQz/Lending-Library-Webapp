package org.example.library.dto;

// DTO to manage a subset of data from the books and loans tables for tabulation

public class BookStats {
    private Long bookCount;
    private String barcode;
    private String title;
    private String author;

    public BookStats(Long bookCount, String barcode, String title, String author) {
        this.bookCount = bookCount;
        this.barcode = barcode;
        this.title = title;
        this.author = author;
    }

    public Long getBookCount() {
        return bookCount;
    }

    public void setBookCount(Long bookCount) {
        this.bookCount = bookCount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
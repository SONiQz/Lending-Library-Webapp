package org.example.library.model.warehouse;

import jakarta.persistence.*;

@Entity
// Define Books Table Model
@Table(name = "Books")
public class warehouseBookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // String Permits Leading Zeros
    private String barcode;
    private String title;

    private String author;

    // Should only be an Int
    private Integer year;

    // String Permits Leading Zeros
    private String isbn;
    private boolean available;

    private String timestamp;
	
    // Constructor with parameters
    public warehouseBookModel(String barcode, String title, Integer year, String isbn, String author) {
        this.barcode = barcode;
        this.title = title;
        this.year = year;
        this.isbn = isbn;
        this.author = author;
        this.available = true;
        this.timestamp = timestamp;
    }

    public warehouseBookModel(String barcode, String title, String author, String timestamp) {
        this.barcode = barcode;
        this.title = title;
        this.author = author;
        this.timestamp = timestamp;
    }
    // Default constructor
    public warehouseBookModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAvailable(boolean available) { this.available = available; }

    public boolean getAvailable() { return available; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getTimestamp() { return timestamp; }
}

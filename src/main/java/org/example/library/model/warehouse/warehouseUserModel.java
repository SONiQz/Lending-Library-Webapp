package org.example.library.model.warehouse;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.SessionAttributes;
@Entity
@SessionAttributes("user")
// Define Table Model
@Table(name = "users")
public class warehouseUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String barcode;
    private String firstName;
    private String lastName;
    private String addr1;
    private String addr2;
    private String city;
    private String postcode;
    private String county;
    private String email;
    private String telephone;



    // Default constructor
    public warehouseUserModel() {
    }

    // Constructor with parameters
    public warehouseUserModel(String barcode, String firstName, String lastName, String addr1, String addr2,
                              String city, String postcode, String county, String email, String telephone) {
        this.barcode = barcode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.postcode = postcode;
        this.county = county;
        this.email = email;
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}

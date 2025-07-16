package org.example.library.model.staging;

import jakarta.persistence.*;

import java.util.Date;

// Definition of the lastXferModel - which is used for identifying when the latest data transfer was completed.

@Entity
@Table(name = "LAST_XFER")

public class lastXferModel {
    @Id
    private String tableName;
    private Date lastDate;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }
}
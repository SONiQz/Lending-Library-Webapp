package org.example.library.repository;

import org.example.library.dto.BookStats;
import org.example.library.model.warehouse.warehouseBookModel;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

// Implementation of Warehouse Book Repo
public interface warehouseBookRepositoryCustom {
    // Defining the parameters for findTopBooks (Its a List)
    List<BookStats> findTopBooks();

    //Defining the parameters for countBooks(i.e. its a LONG)
    long countBooks();

    // Defining the parameters for Edited Books Query (Its a List and takes LocalDate Params)
    List<warehouseBookModel> booksEditedByRange(LocalDate startDate, LocalDate endDate);

    // Defining the parameters for counting books by range (It's a LONG and it takes start and end date params)
    long countBooksByRange(LocalDate startDate, LocalDate endDate);
}

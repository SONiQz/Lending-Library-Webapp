package org.example.library.repository;

import org.example.library.model.warehouse.warehouseUserModel;
import org.example.library.model.warehouse.warehouseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


// Early version of User Repository to use either SpringBoot or Varied PL/SQL - No longer needed
@Repository
public interface warehouseUserRepository extends JpaRepository<warehouseUserModel, String> {
    warehouseUserModel findByBarcode(String barcode);

    // Find UserID from First name or Last Name
    List<warehouseUserModel> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    boolean existsByBarcode(String barcode);

    @Query("SELECT COUNT(*) " +
            "FROM warehouseUserModel")
    List<warehouseUserModel> countTotalUsers();

    @Query ("SELECT COUNT(l.userId) AS frequency, l.userId, u.firstName, u.lastName " +
            "FROM warehouseLoanModel l " +
            "LEFT JOIN warehouseUserModel u ON u.barcode = l.userId " +
            "WHERE l.dateLoaned BETWEEN :startDate AND :endDate " +
            "GROUP BY l.userId, u.firstName, u.lastName " +
            "ORDER BY frequency DESC")
    List<warehouseUserModel> listMostActiveUsers(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
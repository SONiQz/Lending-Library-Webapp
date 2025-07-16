package org.example.library.repository;

import org.example.library.model.warehouse.warehouseDailyStats;
import org.example.library.model.warehouse.warehouseLoanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repo for Loan to combine the Impl and Custom
@Repository
public interface warehouseLoanRepository extends JpaRepository<warehouseLoanModel, Integer>, warehouseLoanRepositoryCustom {
}
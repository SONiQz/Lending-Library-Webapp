package org.example.library.repository;

import org.example.library.model.staging.stagingLoanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Implementation of Staging Loan Repo (Unused) - In case queries were required.
@Repository
public interface stagingLoanRepository extends JpaRepository<stagingLoanModel, String> {
}

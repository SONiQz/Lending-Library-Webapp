package org.example.library.repository;

import org.example.library.model.operational.User;
import org.example.library.model.staging.lastXferModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Base Repository for LastXfer
@Repository
@Transactional("stagingTransactionManager")
public interface LastXferRepository extends JpaRepository<lastXferModel, String> {
}

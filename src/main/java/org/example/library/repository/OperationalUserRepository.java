package org.example.library.repository;

import org.example.library.model.operational.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Implementation of Operational User Repo
@Repository
@Transactional("operationalTransactionManager")
public interface OperationalUserRepository extends JpaRepository<User, Long> {
    // Method for finding user
    User findByEmail(String email);
}
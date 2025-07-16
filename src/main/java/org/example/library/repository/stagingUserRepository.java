package org.example.library.repository;

import org.example.library.model.staging.stagingUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Implementation of Staging User Repo (Unused) - In case queries were required.
@Repository
public interface stagingUserRepository extends JpaRepository<stagingUserModel, String> {
}
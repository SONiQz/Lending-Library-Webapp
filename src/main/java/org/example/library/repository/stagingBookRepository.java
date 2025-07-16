package org.example.library.repository;

import org.example.library.model.staging.stagingBookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Implementation of Staging Book Repo (Unused) - In case queries were required.
@Repository
public interface stagingBookRepository extends JpaRepository<stagingBookModel, String> {
}

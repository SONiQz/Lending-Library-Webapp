package org.example.library.repository;

import org.example.library.model.warehouse.warehouseBookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repo to allow the integration of the Implentation and Custom Repos so that Data sources could be defined.
@Repository
public interface warehouseBookRepository extends JpaRepository<warehouseBookModel, Integer>, warehouseBookRepositoryCustom {
}

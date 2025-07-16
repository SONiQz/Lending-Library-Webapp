package org.example.library.repository;

import org.example.library.model.warehouse.warehouseDailyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Method for creating a Repo to integrate the Daily Stats Implementation and Custom functions.
@Repository
public interface warehouseDailyStatsRepository extends JpaRepository<warehouseDailyStats, Integer>, warehouseDailyStatsRepositoryCustom  {
}

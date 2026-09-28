package com.stockpulse.stockpulse.repository;

import com.stockpulse.stockpulse.model.InventorySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, Long> {
}
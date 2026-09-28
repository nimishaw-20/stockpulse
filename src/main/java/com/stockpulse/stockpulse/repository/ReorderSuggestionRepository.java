package com.stockpulse.stockpulse.repository;

import com.stockpulse.stockpulse.model.ReorderSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReorderSuggestionRepository extends JpaRepository<ReorderSuggestion, Long> {
}
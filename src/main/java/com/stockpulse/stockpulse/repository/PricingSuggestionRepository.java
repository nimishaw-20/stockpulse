package com.stockpulse.stockpulse.repository;

import com.stockpulse.stockpulse.model.PricingSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingSuggestionRepository extends JpaRepository<PricingSuggestion, Long> {
}
package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.PricingSuggestion;
import com.stockpulse.stockpulse.model.TriggerType;
import com.stockpulse.stockpulse.strategy.PricingStrategy;
import com.stockpulse.stockpulse.strategy.RuleBasedPricingStrategy;
import org.springframework.stereotype.Service;

@Service
public class RecommendationEngine {
    
    private final PricingStrategy pricingStrategy;
    
    public RecommendationEngine(RuleBasedPricingStrategy ruleBasedPricingStrategy) {
        this.pricingStrategy = ruleBasedPricingStrategy;
    }
    
    /**
     * Generates a pricing recommendation for a product based on a trigger type
     * 
     * @param product the product to generate a recommendation for
     * @param triggerType the trigger that initiated the recommendation
     * @return a PricingSuggestion with the recommended price and supporting information
     */
    public PricingSuggestion generatePricingRecommendation(Product product, TriggerType triggerType) {
        return pricingStrategy.generatePricingSuggestion(product, triggerType);
    }
}
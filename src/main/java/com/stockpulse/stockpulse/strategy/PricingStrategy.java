package com.stockpulse.stockpulse.strategy;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.PricingSuggestion;
import com.stockpulse.stockpulse.model.TriggerType;

public interface PricingStrategy {
    
    /**
     * Given a product and a trigger, calculate a pricing suggestion.
     * 
     * @param product the product to generate a pricing suggestion for
     * @param triggerType the trigger that initiated the recommendation
     * @return a PricingSuggestion with the recommended price and supporting information
     */
    PricingSuggestion generatePricingSuggestion(Product product, TriggerType triggerType);
}
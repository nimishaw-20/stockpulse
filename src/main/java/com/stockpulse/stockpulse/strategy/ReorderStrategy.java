package com.stockpulse.stockpulse.strategy;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.ReorderSuggestion;
import com.stockpulse.stockpulse.model.TriggerType;

public interface ReorderStrategy {
    
    /**
     * Given a product and a trigger, calculate a reorder suggestion.
     * 
     * @param product the product to generate a reorder suggestion for
     * @param triggerType the trigger that initiated the recommendation
     * @return a ReorderSuggestion with the recommended quantity and supporting information
     */
    ReorderSuggestion generateReorderSuggestion(Product product, TriggerType triggerType);
}
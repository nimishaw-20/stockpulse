package com.stockpulse.stockpulse.strategy;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.ReorderSuggestion;
import com.stockpulse.stockpulse.model.SuggestionStatus;
import com.stockpulse.stockpulse.model.TriggerType;
import org.springframework.stereotype.Component;

@Component
public class RuleBasedReorderStrategy implements ReorderStrategy {
    
    @Override
    public ReorderSuggestion generateReorderSuggestion(Product product, TriggerType triggerType) {
        // Calculate recommended quantity using the formula
        int recommendedQuantity = (product.getReorderThreshold() * 2) - product.getStockLevel();
        
        // If the calculated quantity is less than 0, use 0
        if (recommendedQuantity < 0) {
            recommendedQuantity = 0;
        }
        
        double confidence = 0.90;
        String reasoning = String.format(
            "Current stock: %d, Reorder threshold: %d, Recommended reorder quantity: %d", 
            product.getStockLevel(), product.getReorderThreshold(), recommendedQuantity
        );
        
        ReorderSuggestion reorderSuggestion = new ReorderSuggestion();
        reorderSuggestion.setProduct(product);
        reorderSuggestion.setRecommendedQuantity(recommendedQuantity);
        reorderSuggestion.setConfidence(confidence);
        reorderSuggestion.setReasoning(reasoning);
        reorderSuggestion.setTriggerType(triggerType);
        reorderSuggestion.setStatus(SuggestionStatus.PENDING);
        reorderSuggestion.setCreatedAt(java.time.LocalDateTime.now());
        
        return reorderSuggestion;
    }
}
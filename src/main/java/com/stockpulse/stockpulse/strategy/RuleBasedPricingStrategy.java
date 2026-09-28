package com.stockpulse.stockpulse.strategy;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.PricingSuggestion;
import com.stockpulse.stockpulse.model.SuggestionStatus;
import com.stockpulse.stockpulse.model.TriggerType;
import org.springframework.stereotype.Component;

@Component
public class RuleBasedPricingStrategy implements PricingStrategy {
    
    @Override
    public PricingSuggestion generatePricingSuggestion(Product product, TriggerType triggerType) {
        double currentPrice = product.getCurrentPrice();
        double recommendedPrice;
        double confidence;
        String reasoning;
        
        switch (triggerType) {
            case LOW_STOCK:
                recommendedPrice = currentPrice * 1.10;
                confidence = 0.90;
                reasoning = "Stock is below the reorder threshold, so a 10% price increase is recommended.";
                break;
                
            case DEMAND_SPIKE:
                recommendedPrice = currentPrice * 1.15;
                confidence = 0.90;
                reasoning = "Demand velocity is high, so a 15% price increase is recommended.";
                break;
                
            case MANUAL:
            default:
                recommendedPrice = currentPrice;
                confidence = 0.50;
                reasoning = "No automatic pricing trigger is present.";
                break;
        }
        
        PricingSuggestion pricingSuggestion = new PricingSuggestion();
        pricingSuggestion.setProduct(product);
        pricingSuggestion.setCurrentPrice(currentPrice);
        pricingSuggestion.setRecommendedPrice(recommendedPrice);
        pricingSuggestion.setConfidence(confidence);
        pricingSuggestion.setReasoning(reasoning);
        pricingSuggestion.setTriggerType(triggerType);
        pricingSuggestion.setStatus(SuggestionStatus.PENDING);
        pricingSuggestion.setCreatedAt(java.time.LocalDateTime.now());
        
        return pricingSuggestion;
    }
}
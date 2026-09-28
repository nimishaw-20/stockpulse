package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.TriggerType;
import org.springframework.stereotype.Service;

@Service
public class TriggerService {
    
    /**
     * Checks if a LOW_STOCK trigger is detected for a given product
     * @param product the product to check
     * @return TriggerResult indicating whether LOW_STOCK trigger was detected
     */
    public TriggerResult checkLowStockTrigger(Product product) {
        if (product.getStockLevel() < product.getReorderThreshold()) {
            return new TriggerResult(true, TriggerType.LOW_STOCK, 
                "LOW_STOCK trigger detected for product: " + product.getSku());
        }
        return new TriggerResult(false, null, null);
    }
    
    /**
     * Checks if a DEMAND_SPIKE trigger is detected for a given product
     * @param product the product to check
     * @return TriggerResult indicating whether DEMAND_SPIKE trigger was detected
     */
    public TriggerResult checkDemandSpikeTrigger(Product product) {
        if (product.getDemandVelocity() >= 10) {
            return new TriggerResult(true, TriggerType.DEMAND_SPIKE, 
                "DEMAND_SPIKE trigger detected for product: " + product.getSku());
        }
        return new TriggerResult(false, null, null);
    }
    
    /**
     * Simple result class to encapsulate trigger detection results
     */
    public static class TriggerResult {
        private final boolean triggered;
        private final TriggerType triggerType;
        private final String message;
        
        public TriggerResult(boolean triggered, TriggerType triggerType, String message) {
            this.triggered = triggered;
            this.triggerType = triggerType;
            this.message = message;
        }
        
        public boolean isTriggered() {
            return triggered;
        }
        
        public TriggerType getTriggerType() {
            return triggerType;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
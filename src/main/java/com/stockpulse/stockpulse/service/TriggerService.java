package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.TriggerType;
import org.springframework.stereotype.Service;

@Service
public class TriggerService {

    /**
     * Checks if a INVENTORY_LOW trigger is detected for a given product
     * @param product the product to check
     * @return TriggerResult indicating whether INVENTORY_LOW trigger was detected
     */
    public TriggerResult checkLowStockTrigger(Product product) {
        if (product.getStockLevel() < product.getReorderThreshold()) {
            return new TriggerResult(true, TriggerType.INVENTORY_LOW, 
                "INVENTORY_LOW trigger detected for product: " + product.getSku());
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
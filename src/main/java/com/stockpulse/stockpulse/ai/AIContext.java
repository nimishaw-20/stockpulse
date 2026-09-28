package com.stockpulse.stockpulse.ai;

import com.stockpulse.stockpulse.model.TriggerType;

public class AIContext {
    private String productName;
    private String category;
    private double currentPrice;
    private int stockLevel;
    private int reorderThreshold;
    private double demandVelocity;
    private TriggerType triggerType;
    
    // No-argument constructor
    public AIContext() {
    }
    
    // Constructor with all fields
    public AIContext(String productName, String category, double currentPrice, int stockLevel, 
                     int reorderThreshold, double demandVelocity, TriggerType triggerType) {
        this.productName = productName;
        this.category = category;
        this.currentPrice = currentPrice;
        this.stockLevel = stockLevel;
        this.reorderThreshold = reorderThreshold;
        this.demandVelocity = demandVelocity;
        this.triggerType = triggerType;
    }
    
    // Getters
    public String getProductName() {
        return productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    public int getStockLevel() {
        return stockLevel;
    }
    
    public int getReorderThreshold() {
        return reorderThreshold;
    }
    
    public double getDemandVelocity() {
        return demandVelocity;
    }
    
    public TriggerType getTriggerType() {
        return triggerType;
    }
    
    // Setters
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }
    
    public void setReorderThreshold(int reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }
    
    public void setDemandVelocity(double demandVelocity) {
        this.demandVelocity = demandVelocity;
    }
    
    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }
}
package com.stockpulse.stockpulse.ai;

public class AIPricingResponse {
    private double recommendedPrice;
    private double confidence;
    private String reasoning;
    
    // No-argument constructor
    public AIPricingResponse() {
    }
    
    // Constructor with all fields
    public AIPricingResponse(double recommendedPrice, double confidence, String reasoning) {
        this.recommendedPrice = recommendedPrice;
        this.confidence = confidence;
        this.reasoning = reasoning;
    }
    
    // Getters
    public double getRecommendedPrice() {
        return recommendedPrice;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public String getReasoning() {
        return reasoning;
    }
    
    // Setters
    public void setRecommendedPrice(double recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }
    
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
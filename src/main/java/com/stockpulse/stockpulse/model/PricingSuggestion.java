package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
public class PricingSuggestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @PositiveOrZero
    private double currentPrice;
    
    @PositiveOrZero
    private double recommendedPrice;
    
    @PositiveOrZero
    private double confidence;
    
    private String reasoning;
    
    @Enumerated(EnumType.STRING)
    private TriggerType triggerType;
    
    @Enumerated(EnumType.STRING)
    private SuggestionStatus status = SuggestionStatus.PENDING;
    
    private LocalDateTime createdAt;
    
    // No-argument constructor required by JPA
    public PricingSuggestion() {}
    
    // Constructor
    public PricingSuggestion(Product product, double currentPrice, double recommendedPrice, 
                             double confidence, String reasoning, TriggerType triggerType, 
                             LocalDateTime createdAt) {
        this.product = product;
        this.currentPrice = currentPrice;
        this.recommendedPrice = recommendedPrice;
        this.confidence = confidence;
        this.reasoning = reasoning;
        this.triggerType = triggerType;
        this.createdAt = createdAt;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    public double getRecommendedPrice() {
        return recommendedPrice;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public String getReasoning() {
        return reasoning;
    }
    
    public TriggerType getTriggerType() {
        return triggerType;
    }
    
    public SuggestionStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public void setRecommendedPrice(double recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }
    
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
    
    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }
    
    public void setStatus(SuggestionStatus status) {
        this.status = status;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
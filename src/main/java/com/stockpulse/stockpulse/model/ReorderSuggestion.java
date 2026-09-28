package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
public class ReorderSuggestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @PositiveOrZero
    private int currentStock;
    
    @PositiveOrZero
    private int recommendedQuantity;
    
    @PositiveOrZero
    private int suggestedLeadTimeDays;
    
    @PositiveOrZero
    private double confidence;
    
    private String reasoning;
    
    @Enumerated(EnumType.STRING)
    private SuggestionStatus status = SuggestionStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    private TriggerType triggerReason;
    
    private LocalDateTime createdAt;
    
    // No-argument constructor required by JPA
    public ReorderSuggestion() {}
    
    // Constructor
    public ReorderSuggestion(Product product, int currentStock, int recommendedQuantity, 
                             int suggestedLeadTimeDays, double confidence, String reasoning, 
                             TriggerType triggerReason, LocalDateTime createdAt) {
        this.product = product;
        this.currentStock = currentStock;
        this.recommendedQuantity = recommendedQuantity;
        this.suggestedLeadTimeDays = suggestedLeadTimeDays;
        this.confidence = confidence;
        this.reasoning = reasoning;
        this.triggerReason = triggerReason;
        this.createdAt = createdAt;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getCurrentStock() {
        return currentStock;
    }
    
    public int getRecommendedQuantity() {
        return recommendedQuantity;
    }
    
    public int getSuggestedLeadTimeDays() {
        return suggestedLeadTimeDays;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public String getReasoning() {
        return reasoning;
    }
    
    public SuggestionStatus getStatus() {
        return status;
    }
    
    public TriggerType getTriggerReason() {
        return triggerReason;
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
    
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }
    
    public void setRecommendedQuantity(int recommendedQuantity) {
        this.recommendedQuantity = recommendedQuantity;
    }
    
    public void setSuggestedLeadTimeDays(int suggestedLeadTimeDays) {
        this.suggestedLeadTimeDays = suggestedLeadTimeDays;
    }
    
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
    
    public void setStatus(SuggestionStatus status) {
        this.status = status;
    }
    
    public void setTriggerReason(TriggerType triggerReason) {
        this.triggerReason = triggerReason;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
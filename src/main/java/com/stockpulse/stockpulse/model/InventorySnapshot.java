package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
public class InventorySnapshot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @PositiveOrZero
    private int stockLevel;
    
    @PositiveOrZero
    private double demandVelocity;
    
    private LocalDateTime capturedAt;
    
    // No-argument constructor required by JPA
    public InventorySnapshot() {}
    
    // Constructor
    public InventorySnapshot(Product product, int stockLevel, double demandVelocity, LocalDateTime capturedAt) {
        this.product = product;
        this.stockLevel = stockLevel;
        this.demandVelocity = demandVelocity;
        this.capturedAt = capturedAt;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getStockLevel() {
        return stockLevel;
    }
    
    public double getDemandVelocity() {
        return demandVelocity;
    }
    
    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }
    
    public void setDemandVelocity(double demandVelocity) {
        this.demandVelocity = demandVelocity;
    }
    
    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }
}
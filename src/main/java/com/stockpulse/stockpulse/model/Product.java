package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    @NotBlank
    private String sku;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String category;
    
    @PositiveOrZero
    private double currentPrice;
    
    @PositiveOrZero
    private int stockLevel;
    
    @PositiveOrZero
    private int reorderThreshold;
    
    @PositiveOrZero
    private double demandVelocity;
    
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    private Double costPrice;
    
    // No-argument constructor required by JPA
    public Product() {}
    
    // Constructor with required fields
    public Product(String sku, String name, String category, double currentPrice, 
                   int stockLevel, int reorderThreshold, double demandVelocity) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.stockLevel = stockLevel;
        this.reorderThreshold = reorderThreshold;
        this.demandVelocity = demandVelocity;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getSku() {
        return sku;
    }
    
    public String getName() {
        return name;
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
    
    public ProductStatus getStatus() {
        return status;
    }
    
    public Double getCostPrice() {
        return costPrice;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }
}
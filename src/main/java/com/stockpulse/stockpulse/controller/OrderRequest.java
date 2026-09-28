package com.stockpulse.stockpulse.controller;

public class OrderRequest {
    private Long productId;
    private int quantity;
    
    // No-argument constructor
    public OrderRequest() {}
    
    // Constructor
    public OrderRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters
    public Long getProductId() {
        return productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    // Setters
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Positive
    private int quantity;
    
    private LocalDateTime createdAt;
    
    // No-argument constructor required by JPA
    public Order() {}
    
    // Constructor
    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
    }
    
    // PrePersist callback to automatically set createdAt
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
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
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
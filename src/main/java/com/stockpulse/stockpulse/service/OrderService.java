package com.stockpulse.stockpulse.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stockpulse.stockpulse.model.Order;
import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.repository.OrderRepository;
import com.stockpulse.stockpulse.repository.ProductRepository;
import com.stockpulse.stockpulse.service.TriggerService.TriggerResult;

@Service
public class OrderService {
    
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final TriggerService triggerService;
    private final AsyncRecommendationService asyncRecommendationService;
    
    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, 
                       TriggerService triggerService, AsyncRecommendationService asyncRecommendationService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.triggerService = triggerService;
        this.asyncRecommendationService = asyncRecommendationService;
    }
    
    public Order createOrder(Long productId, int quantity) {
        // Validate quantity
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        
        // Find the product by ID
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product product = productOptional.get();
        
        // Check if there is sufficient stock
        if (quantity > product.getStockLevel()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                                     ". Available: " + product.getStockLevel() + ", Requested: " + quantity);
        }
        
        // Reduce stock level
        product.setStockLevel(product.getStockLevel() - quantity);
        productRepository.save(product);
        
        // Check for LOW_STOCK trigger after reducing stock
        TriggerResult lowStockResult = triggerService.checkLowStockTrigger(product);
        if (lowStockResult.isTriggered()) {
            System.out.println(lowStockResult.getMessage());
            // Call async recommendation service for LOW_STOCK trigger
            asyncRecommendationService.processRecommendation(product, lowStockResult.getTriggerType());
        }
        
        // Check for DEMAND_SPIKE trigger
        TriggerResult demandSpikeResult = triggerService.checkDemandSpikeTrigger(product);
        if (demandSpikeResult.isTriggered()) {
            System.out.println(demandSpikeResult.getMessage());
            // Call async recommendation service for DEMAND_SPIKE trigger
            asyncRecommendationService.processRecommendation(product, demandSpikeResult.getTriggerType());
        }
        
        // Create and save the order
        Order order = new Order(product, quantity);
        return orderRepository.save(order);
    }
}




package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.model.Order;
import com.stockpulse.stockpulse.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest.getProductId(), orderRequest.getQuantity());
    }
}
package com.stockpulse.stockpulse.config;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.ProductStatus;
import com.stockpulse.stockpulse.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    public DatabaseSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Check if products already exist to prevent duplicates on restart
        if (productRepository.count() == 0) {
            // Seed the 8 products
            Product product1 = new Product("PRD-001", "Wireless Earbuds Pro", "ELECTRONICS", 79.99, 25, 10, 4);
            product1.setStatus(ProductStatus.ACTIVE);
            
            Product product2 = new Product("PRD-002", "USB-C Hub 7-Port", "ELECTRONICS", 49.99, 30, 12, 5);
            product2.setStatus(ProductStatus.ACTIVE);
            
            Product product3 = new Product("PRD-003", "Organic Cotton T-Shirt", "APPAREL", 24.99, 8, 15, 12);
            product3.setStatus(ProductStatus.ACTIVE);
            
            Product product4 = new Product("PRD-004", "Running Shorts - Navy", "APPAREL", 29.99, 20, 10, 4);
            product4.setStatus(ProductStatus.ACTIVE);
            
            Product product5 = new Product("PRD-005", "Ceramic Pour-Over Set", "HOME", 39.99, 18, 8, 3);
            product5.setStatus(ProductStatus.ACTIVE);
            
            Product product6 = new Product("PRD-006", "LED Desk Lamp - Dimmable", "HOME", 34.99, 22, 10, 4);
            product6.setStatus(ProductStatus.ACTIVE);
            
            Product product7 = new Product("PRD-007", "Portable Charger 20K", "ELECTRONICS", 59.99, 12, 15, 8);
            product7.setStatus(ProductStatus.ACTIVE);
            
            Product product8 = new Product("PRD-008", "Hoodie - Heather Grey", "APPAREL", 44.99, 35, 12, 6);
            product8.setStatus(ProductStatus.ACTIVE);
            
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);
            productRepository.save(product6);
            productRepository.save(product7);
            productRepository.save(product8);
            
            System.out.println("Database seeded with 8 products");
        } else {
            System.out.println("Database already contains products. Skipping seed.");
        }
    }
}
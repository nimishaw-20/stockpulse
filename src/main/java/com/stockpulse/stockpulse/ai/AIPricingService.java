package com.stockpulse.stockpulse.ai;

import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.PricingSuggestion;
import com.stockpulse.stockpulse.model.TriggerType;
import com.stockpulse.stockpulse.strategy.PricingStrategy;
import com.stockpulse.stockpulse.strategy.RuleBasedPricingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIPricingService {
    
    private final LLMGateway llmGateway;
    private final PricingStrategy ruleBasedPricingStrategy;
    
    @Autowired
    public AIPricingService(LLMGateway llmGateway, RuleBasedPricingStrategy ruleBasedPricingStrategy) {
        this.llmGateway = llmGateway;
        this.ruleBasedPricingStrategy = ruleBasedPricingStrategy;
    }
    
    /**
     * Generate a pricing suggestion using AI with fallback to rule-based strategy
     * 
     * @param product the product to generate pricing for
     * @param triggerType the trigger that caused the pricing suggestion
     * @return a validated pricing suggestion
     */
    public PricingSuggestion generatePricingSuggestionWithFallback(Product product, TriggerType triggerType) {
        try {
            // Create AI context
            AIContext aiContext = new AIContext(
                product.getName(),
                product.getCategory(),
                product.getCurrentPrice(),
                product.getStockLevel(),
                product.getReorderThreshold(),
                product.getDemandVelocity(),
                triggerType
            );
            
            // Get AI response
            AIPricingResponse aiResponse = llmGateway.generateStructuredPricingResponse(aiContext);
            
            // Validate AI response
            if (isValidAIPricingResponse(aiResponse, product.getCurrentPrice())) {
                // Create pricing suggestion from valid AI response
                PricingSuggestion suggestion = new PricingSuggestion();
                suggestion.setProduct(product);
                suggestion.setCurrentPrice(product.getCurrentPrice());
                suggestion.setRecommendedPrice(aiResponse.getRecommendedPrice());
                suggestion.setConfidence(aiResponse.getConfidence());
                suggestion.setReasoning(aiResponse.getReasoning());
                suggestion.setTriggerType(triggerType);
                suggestion.setStatus(com.stockpulse.stockpulse.model.SuggestionStatus.PENDING);
                suggestion.setCreatedAt(java.time.LocalDateTime.now());
                return suggestion;
            }
        } catch (Exception e) {
            // Log the exception (in a real application, use proper logging)
            System.err.println("AI pricing generation failed: " + e.getMessage());
        }
        
        // Fallback to rule-based strategy
        return ruleBasedPricingStrategy.generatePricingSuggestion(product, triggerType);
    }
    
    /**
     * Validate the AI pricing response
     * 
     * @param response the AI response to validate
     * @param currentPrice the current price of the product
     * @return true if the response is valid, false otherwise
     */
    private boolean isValidAIPricingResponse(AIPricingResponse response, double currentPrice) {
        if (response == null) {
            return false;
        }
        
        // Check recommended price is positive
        if (response.getRecommendedPrice() <= 0) {
            return false;
        }
        
        // Check recommended price is within a sane range (not more than 10x current price)
        if (response.getRecommendedPrice() > currentPrice * 10) {
            return false;
        }
        
        // Check confidence is between 0.0 and 1.0
        if (response.getConfidence() < 0.0 || response.getConfidence() > 1.0) {
            return false;
        }
        
        // Check reasoning is not null or blank
        if (response.getReasoning() == null || response.getReasoning().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
}
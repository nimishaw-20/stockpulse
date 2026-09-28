package com.stockpulse.stockpulse.ai;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.stockpulse.stockpulse.model.PricingSuggestion;
import com.stockpulse.stockpulse.model.Product;
import com.stockpulse.stockpulse.model.ProductStatus;
import com.stockpulse.stockpulse.model.TriggerType;
import com.stockpulse.stockpulse.strategy.RuleBasedPricingStrategy;

class AIPricingServiceTest {

    @Mock
    private LLMGateway mockLLMGateway;

    private AIPricingService aiPricingService;
    private Product testProduct;
    private RuleBasedPricingStrategy ruleBasedPricingStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ruleBasedPricingStrategy = new RuleBasedPricingStrategy();
        aiPricingService = new AIPricingService(
                mockLLMGateway,
                ruleBasedPricingStrategy
        );

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku("PRD-001");
        testProduct.setName("Wireless Earbuds Pro");
        testProduct.setCategory("ELECTRONICS");
        testProduct.setCurrentPrice(79.99);
        testProduct.setStockLevel(5);
        testProduct.setReorderThreshold(10);
        testProduct.setDemandVelocity(4);
        testProduct.setStatus(ProductStatus.ACTIVE);
    @Test
    void shouldReturnValidAIResponseWhenAllFieldsAreValid() {
        AIPricingResponse validResponse =
                new AIPricingResponse(
                        85.99,
                        0.85,
                        "AI recommends price increase due to market conditions"
                );

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(validResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.INVENTORY_LOW
                );

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(85.99);
        assertThat(suggestion.getConfidence()).isEqualTo(0.85);
        assertThat(suggestion.getReasoning())
                .isEqualTo("AI recommends price increase due to market conditions");
        assertThat(suggestion.getTriggerReason())
                .isEqualTo(TriggerType.INVENTORY_LOW);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }
    }
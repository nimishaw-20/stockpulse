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
    }

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
                        TriggerType.LOW_STOCK
                );

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(85.99);
        assertThat(suggestion.getConfidence()).isEqualTo(0.85);
        assertThat(suggestion.getReasoning())
                .isEqualTo("AI recommends price increase due to market conditions");
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenRecommendedPriceIsZero() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(0.0, 0.85, "Zero price");

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenRecommendedPriceIsNegative() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(-5.0, 0.85, "Invalid negative price");

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenRecommendedPriceIsAbsurdlyHigh() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(1000.0, 0.85, "Absurdly high price");

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenConfidenceIsTooHigh() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(85.99, 1.5, "Invalid confidence");

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenConfidenceIsNegative() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(85.99, -0.5, "Invalid negative confidence");

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenReasoningIsNull() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(85.99, 0.85, null);

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenReasoningIsEmpty() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(85.99, 0.85, "");

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenReasoningIsBlank() {
        AIPricingResponse invalidResponse =
                new AIPricingResponse(85.99, 0.85, "   ");

        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(invalidResponse);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenLLMThrowsException() {
        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenThrow(new RuntimeException("LLM service failed"));

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }

    @Test
    void shouldFallbackToRuleBasedWhenLLMReturnsNull() {
        when(mockLLMGateway.generateStructuredPricingResponse(any(AIContext.class)))
                .thenReturn(null);

        PricingSuggestion suggestion =
                aiPricingService.generatePricingSuggestionWithFallback(
                        testProduct,
                        TriggerType.LOW_STOCK
                );

        double expectedPrice = testProduct.getCurrentPrice() * 1.10;

        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getRecommendedPrice()).isEqualTo(expectedPrice);
        assertThat(suggestion.getConfidence()).isEqualTo(0.90);
        assertThat(suggestion.getTriggerType())
                .isEqualTo(TriggerType.LOW_STOCK);

        verify(mockLLMGateway, times(1))
                .generateStructuredPricingResponse(any(AIContext.class));
    }
}
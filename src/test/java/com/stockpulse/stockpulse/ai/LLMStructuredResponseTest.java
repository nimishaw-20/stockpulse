package com.stockpulse.stockpulse.ai;

import com.stockpulse.stockpulse.model.TriggerType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LLMStructuredResponseTest {

    @Test
    void shouldGenerateStructuredPricingResponse() {
        // Arrange
        LLMGateway llmGateway = new LLMGateway();
        AIContext aiContext = new AIContext(
            "Organic Cotton T-Shirt",
            "APPAREL",
            24.99,
            7,
            15,
            12,
            TriggerType.LOW_STOCK
        );

        // Act
        AIPricingResponse response = llmGateway.generateStructuredPricingResponse(aiContext);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRecommendedPrice()).isGreaterThanOrEqualTo(0);
        assertThat(response.getConfidence()).isBetween(0.0, 1.0);
        assertThat(response.getReasoning()).isNotNull().isNotEmpty();
        
        System.out.println("Recommended Price: $" + response.getRecommendedPrice());
        System.out.println("Confidence: " + response.getConfidence());
        System.out.println("Reasoning: " + response.getReasoning());
    }
}
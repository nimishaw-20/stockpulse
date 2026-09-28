package com.stockpulse.stockpulse.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LLMGatewayTest {

    @Test
    void shouldGenerateResponseFromGroq() {
        // Arrange
        LLMGateway llmGateway = new LLMGateway();
        String prompt = "Reply with exactly: StockPulse AI works";
        
        // Act
        String response = llmGateway.generateResponse(prompt);
        
        // Assert
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        System.out.println("LLM Response: " + response);
    }
}
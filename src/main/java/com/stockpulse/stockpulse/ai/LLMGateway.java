package com.stockpulse.stockpulse.ai;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class LLMGateway {
    
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_MODEL = "openai/gpt-oss-20b";
    private static final String API_KEY_ENV_VAR = "GROQ_API_KEY";
    
    private final HttpClient httpClient;
    private final String apiKey;
    
    public LLMGateway() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        
        // Read API key from environment variable
        this.apiKey = System.getenv(API_KEY_ENV_VAR);
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("GROQ_API_KEY environment variable is not configured");
        }
    }
    
    /**
     * Generate a response from the LLM using the provided prompt
     * 
     * @param prompt the text prompt to send to the LLM
     * @return the generated text response
     */
    public String generateResponse(String prompt) {
        try {
            // Create JSON request body
            String requestBody = createJsonRequestBody(prompt);
            
            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_API_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            
            // Send request and get response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Check if request was successful
            if (response.statusCode() != 200) {
                throw new RuntimeException("Groq API request failed with status code: " + response.statusCode() + 
                                         ", response: " + response.body());
            }
            
            // Extract generated text from response
            return extractGeneratedText(response.body());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate response from LLM: " + e.getMessage(), e);
        }
    }
    
    /**
     * Generate a response from the LLM using AIContext to build a structured prompt
     * 
     * @param aiContext the context information for the AI
     * @return the generated text response
     */
    public String generateResponse(AIContext aiContext) {
        String prompt = buildPromptFromContext(aiContext);
        return generateResponse(prompt);
    }
    
    /**
     * Generate a structured pricing response from the LLM using AIContext
     * 
     * @param aiContext the context information for the AI
     * @return the parsed AIPricingResponse object
     */
    public AIPricingResponse generateStructuredPricingResponse(AIContext aiContext) {
        String response = generateResponse(aiContext);
        return parseAIPricingResponse(response);
    }
    
    /**
     * Parse the AI response into an AIPricingResponse object
     * 
     * @param response the raw JSON response from the AI
     * @return the parsed AIPricingResponse object
     */
    private AIPricingResponse parseAIPricingResponse(String response) {
        try {
            // Extract JSON from response - the response may be escaped JSON or raw JSON
            String jsonContent = extractJsonContent(response);
            
            // Parse the JSON manually without external libraries
            return parseJsonToAIPricingResponse(jsonContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI pricing response: " + e.getMessage() + 
                                     ", response: " + response, e);
        }
    }
    
    /**
     * Extract JSON content from the response
     */
    private String extractJsonContent(String response) {
        // First try to find JSON object in the response
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        
        if (start != -1 && end != -1 && end > start) {
            String potentialJson = response.substring(start, end + 1);
            
            // Check if this looks like valid JSON by ensuring it has the required fields
            if (potentialJson.contains("\"recommendedPrice\"") && 
                potentialJson.contains("\"confidence\"") && 
                potentialJson.contains("\"reasoning\"")) {
                return potentialJson;
            }
        }
        
        // If we can't find proper JSON with our fields, return the whole response
        // The parsing methods will handle the error appropriately
        return response;
    }
    
    /**
     * Parse JSON string to AIPricingResponse object
     */
    private AIPricingResponse parseJsonToAIPricingResponse(String jsonString) {
        try {
            // Clean up the JSON string in case it's escaped
            String cleanJson = jsonString.trim();
            
            // Manual JSON parsing without external libraries
            double recommendedPrice = parseDoubleField(cleanJson, "recommendedPrice");
            double confidence = parseDoubleField(cleanJson, "confidence");
            String reasoning = parseStringField(cleanJson, "reasoning");
            
            return new AIPricingResponse(recommendedPrice, confidence, reasoning);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to AIPricingResponse: " + e.getMessage() + 
                                     ", JSON: " + jsonString, e);
        }
    }
    
    /**
     * Parse a double field from JSON string
     */
    private double parseDoubleField(String jsonString, String fieldName) {
        String marker = "\"" + fieldName + "\":";
        String escapedMarker = "\\\"" + fieldName + "\\\":"; 
        
        // Try both regular and escaped markers
        int start = jsonString.indexOf(marker);
        if (start == -1) {
            start = jsonString.indexOf(escapedMarker);
            if (start != -1) {
                marker = escapedMarker;
            }
        }
        
        if (start == -1) {
            throw new RuntimeException("Field '" + fieldName + "' not found in JSON");
        }
        
        start += marker.length();
        // Skip whitespace
        while (start < jsonString.length() && Character.isWhitespace(jsonString.charAt(start))) {
            start++;
        }
        
        // Find end of value
        int end = start;
        if (jsonString.charAt(start) == '\"' || (start < jsonString.length() - 1 && 
            jsonString.charAt(start) == '\\' && jsonString.charAt(start + 1) == '\"')) {
            // String value (handle escaped quotes)
            if (jsonString.charAt(start) == '\\') {
                start += 2; // Skip escaped quote
            } else {
                start++; // Skip quote
            }
            // Look for closing quote (handle escaped quotes)
            end = start;
            while (end < jsonString.length()) {
                if (jsonString.charAt(end) == '\"' && (end == 0 || jsonString.charAt(end - 1) != '\\')) {
                    break;
                }
                end++;
            }
        } else {
            // Numeric value
            while (end < jsonString.length() && (Character.isDigit(jsonString.charAt(end)) || 
                   jsonString.charAt(end) == '.' || jsonString.charAt(end) == '-')) {
                end++;
            }
        }
        
        if (end == -1 || end >= jsonString.length()) {
            throw new RuntimeException("Could not parse value for field '" + fieldName + "'");
        }
        
        String valueStr = jsonString.substring(start, end).trim();
        // Handle escaped quotes in the value
        valueStr = valueStr.replace("\\\"", "\"");
        return Double.parseDouble(valueStr);
    }
    
    /**
     * Parse a string field from JSON string
     */
    private String parseStringField(String jsonString, String fieldName) {
        String marker = "\"" + fieldName + "\":";
        String escapedMarker = "\\\"" + fieldName + "\\\":"; 
        
        // Try both regular and escaped markers
        int start = jsonString.indexOf(marker);
        if (start == -1) {
            start = jsonString.indexOf(escapedMarker);
            if (start != -1) {
                marker = escapedMarker;
            }
        }
        
        if (start == -1) {
            throw new RuntimeException("Field '" + fieldName + "' not found in JSON");
        }
        
        start += marker.length();
        // Skip whitespace
        while (start < jsonString.length() && Character.isWhitespace(jsonString.charAt(start))) {
            start++;
        }
        
        // Handle both escaped and unescaped quotes
        boolean isEscapedQuote = (start < jsonString.length() - 1 && 
                                 jsonString.charAt(start) == '\\' && jsonString.charAt(start + 1) == '\"');
        
        if (jsonString.charAt(start) == '\"' || isEscapedQuote) {
            // String value
            if (isEscapedQuote) {
                start += 2; // Skip escaped quote
            } else {
                start++; // Skip quote
            }
            
            // Look for closing quote (handle escaped quotes)
            int end = start;
            while (end < jsonString.length()) {
                if (jsonString.charAt(end) == '\"' && (end == 0 || jsonString.charAt(end - 1) != '\\')) {
                    break;
                }
                end++;
            }
            
            if (end >= jsonString.length()) {
                throw new RuntimeException("Could not parse string value for field '" + fieldName + "'");
            }
            
            // Handle escaped characters
            String escapedValue = jsonString.substring(start, end);
            return unescapeJson(escapedValue);
        } else {
            // Non-string value
            int end = start;
            while (end < jsonString.length() && !Character.isWhitespace(jsonString.charAt(end)) && 
                   jsonString.charAt(end) != ',' && jsonString.charAt(end) != '}') {
                end++;
            }
            
            return jsonString.substring(start, end).trim();
        }
    }
    
    /**
     * Build a structured prompt from AIContext
     */
    private String buildPromptFromContext(AIContext aiContext) {
        return "Given the following product information:\n" +
               "- Product Name: " + aiContext.getProductName() + "\n" +
               "- Category: " + aiContext.getCategory() + "\n" +
               "- Current Price: $" + aiContext.getCurrentPrice() + "\n" +
               "- Stock Level: " + aiContext.getStockLevel() + "\n" +
               "- Reorder Threshold: " + aiContext.getReorderThreshold() + "\n" +
               "- Demand Velocity: " + aiContext.getDemandVelocity() + "\n" +
               "- Trigger Type: " + aiContext.getTriggerType() + "\n\n" +
               "Provide a pricing recommendation in the following exact JSON format:\n" +
               "{\n" +
               "  \"recommendedPrice\": 0.0,\n" +
               "  \"confidence\": 0.0,\n" +
               "  \"reasoning\": \"...\"\n" +
               "}\n\n" +
               "Rules:\n" +
               "- recommendedPrice must be a non-negative number\n" +
               "- confidence must be between 0 and 1\n" +
               "- reasoning should briefly explain the recommendation\n" +
               "- Return only valid JSON\n" +
               "- No markdown\n" +
               "- No extra text outside the JSON";
    }
    
    /**
     * Create JSON request body for the Groq API
     */
    private String createJsonRequestBody(String prompt) {
        // Create JSON manually to avoid text blocks which might not be supported
        return "{\n" +
               "  \"model\": \"" + GROQ_MODEL + "\",\n" +
               "  \"messages\": [\n" +
               "    {\n" +
               "      \"role\": \"user\",\n" +
               "      \"content\": \"" + escapeJson(prompt) + "\"\n" +
               "    }\n" +
               "  ],\n" +
               "  \"temperature\": 0.7\n" +
               "}";
    }
    
    /**
     * Extract generated text from the Groq API response
     */
    private String extractGeneratedText(String responseBody) {
        // Simple parsing of JSON response to extract the generated text
        // This is a basic implementation without external JSON libraries
        try {
            // Navigate the Groq API response structure more robustly
            
            // Find "choices" array
            int choicesIndex = responseBody.indexOf("\"choices\"");
            if (choicesIndex == -1) {
                throw new RuntimeException("Could not find 'choices' in response");
            }
            
            // Find the opening bracket of choices array
            int choicesOpenBracket = responseBody.indexOf("[", choicesIndex);
            if (choicesOpenBracket == -1) {
                throw new RuntimeException("Could not find choices array opening bracket");
            }
            
            // Find the closing bracket (we'll work within the first choice)
            int choicesCloseBracket = findMatchingBracket(responseBody, choicesOpenBracket);
            if (choicesCloseBracket == -1) {
                throw new RuntimeException("Could not find choices array closing bracket");
            }
            
            // Work within the first choice (between the first [ and first ])
            String firstChoiceSection = responseBody.substring(choicesOpenBracket + 1, choicesCloseBracket);
            
            // Find "message" in the first choice
            int messageIndex = firstChoiceSection.indexOf("\"message\"");
            if (messageIndex == -1) {
                throw new RuntimeException("Could not find 'message' in first choice");
            }
            
            // Find "content" after message
            int contentIndex = firstChoiceSection.indexOf("\"content\"", messageIndex);
            if (contentIndex == -1) {
                throw new RuntimeException("Could not find 'content' in message");
            }
            
            // Find the colon after "content"
            int colonIndex = firstChoiceSection.indexOf(":", contentIndex);
            if (colonIndex == -1) {
                throw new RuntimeException("Could not find colon after 'content'");
            }
            
            // Find the start of the value (skip whitespace)
            int valueStart = colonIndex + 1;
            while (valueStart < firstChoiceSection.length() && 
                   Character.isWhitespace(firstChoiceSection.charAt(valueStart))) {
                valueStart++;
            }
            
            // Handle string value
            if (valueStart < firstChoiceSection.length() && firstChoiceSection.charAt(valueStart) == '"') {
                valueStart++; // Skip opening quote
                int valueEnd = findUnescapedQuote(firstChoiceSection, valueStart);
                if (valueEnd == -1) {
                    throw new RuntimeException("Could not find closing quote for content value");
                }
                
                String contentValue = firstChoiceSection.substring(valueStart, valueEnd);
                return unescapeJson(contentValue);
            } else {
                // Non-string value
                int valueEnd = valueStart;
                while (valueEnd < firstChoiceSection.length() && 
                       firstChoiceSection.charAt(valueEnd) != ',' && 
                       firstChoiceSection.charAt(valueEnd) != '}' &&
                       firstChoiceSection.charAt(valueEnd) != ']') {
                    valueEnd++;
                }
                return firstChoiceSection.substring(valueStart, valueEnd).trim();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq API response: " + e.getMessage() + 
                                     ", response body: " + responseBody, e);
        }
    }
    
    /**
     * Find the position of an unescaped quote in a JSON string
     */
    private int findUnescapedQuote(String text, int startPos) {
        for (int i = startPos; i < text.length(); i++) {
            if (text.charAt(i) == '"' && (i == 0 || text.charAt(i - 1) != '\\')) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Find matching closing bracket for an opening bracket
     */
    private int findMatchingBracket(String text, int openBracketPos) {
        int bracketCount = 1;
        for (int i = openBracketPos + 1; i < text.length(); i++) {
            if (text.charAt(i) == '[') {
                bracketCount++;
            } else if (text.charAt(i) == ']') {
                bracketCount--;
                if (bracketCount == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Escape special characters in JSON strings
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Unescape special characters from JSON strings
     */
    private String unescapeJson(String text) {
        return text.replace("\\\"", "\"")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t")
                  .replace("\\\\", "\\");
    }
}
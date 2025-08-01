package se.lexicon.flightbooking_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Service
public class OpenAiService {

    private final WebClient webClient;
    private final ChatHistoryService chatHistoryService;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    public OpenAiService(WebClient.Builder webClientBuilder, ChatHistoryService chatHistoryService) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.chatHistoryService = chatHistoryService;

    }

    public Mono<String> getChatResponse(String email, String message, String content) {
        chatHistoryService.addMessage(email, message, content);
        List<Map<String, String>> chatHistory = chatHistoryService.getHistory(email);

        String requestBody = buildRequestBody(chatHistory);

        return webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    chatHistoryService.addMessage(email, "assistant", response);
                });
    }

    private String buildRequestBody(List<Map<String, String>> chatHistory) {
        return """
                {
                    "model": gpt-4",
                    "messages": [
                        {
                            "role": "system",
                            "content": "
                                * You are a customer support assistant for an airline company named "FokkAirlines".
                                * You are friendly and helpful in a strict way.
                                *The customers can ask you about flights they wanto book, and about their bookings.
                                * Before answering anything about their bookings they must provide their email-adress, 
                                and if you cant find the adress in the system you will tell the customer that "I am sorry, but i cant find any bookings with the email"
                                * If the user asks about help to book a flight, check the chat history to be as forthcoming as possible."
                                },
                                {
                                    "role": "user",
                                    "content": "%s"
                                }
                            ]
                                
                }
                """.formatted(messages);
    }








}

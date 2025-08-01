package se.lexicon.flightbooking_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.lexicon.flightbooking_api.dto.*;
import se.lexicon.flightbooking_api.service.AiTools;
import se.lexicon.flightbooking_api.service.AiToolsService;
import se.lexicon.flightbooking_api.service.FlightBookingService;
import se.lexicon.flightbooking_api.service.OpenAiService;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatBotController {



    private final FlightBookingService flightBookingService;
    private final OpenAiService openAiService;

    @PostMapping("/message")
    public Mono<ResponseEntity<ChatResponse>> handleMessage(@RequestBody ChatBotUserMessageDTO chatRequest) {

        String email = chatRequest.getEmail();
        String message = chatRequest.getMessage();

        return openAiService.getChatResponse(email, message, "user")
                .map(aiResponse -> new Generation(new AssistantMessage(aiResponse)))
                .flatMap(generation ->
                 AiToolsService.parse(generation)
                        .map(toolCall -> handleToolCall(toolCall, chatRequest)
                                .map(result -> ResponseEntity.ok(
                                        new ChatResponse(List.of(new Generation(new AssistantMessage(result)))))))
                        .orElseGet(() -> Mono.just(ResponseEntity.ok(
                                new ChatResponse(List.of(generation))
                        ))));
    }

    private Mono<String> handleToolCall(AiTools toolCall, ChatBotUserMessageDTO chatRequest) {
        switch (toolCall.getTool()) {
            case "searchFlights":
                return Mono.just(flightBookingService.findAvailableFlights().toString());
            case "bookFlight":
                Long flightId = toolCall.getParams().get("flightId").asLong();
                String email = chatRequest.getEmail();
                String passengerName = toolCall.getParams().get("passengerName").asText();
                return Mono.just("Flight booked for " + passengerName + " (Id: " + flightId + ")");
            case "cancelBooking":
                Long cancelId = toolCall.getParams().get("flightId").asLong();
                return Mono.just("Flight cancelled (Id: " + cancelId + ")");
            default:
                return Mono.just("Unknown tool call: " + toolCall.getTool());
        }
    }
}

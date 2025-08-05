package se.lexicon.flightbooking_api.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.ChatDTO.ChatRequest;
import se.lexicon.flightbooking_api.dto.ChatDTO.ChatMessage;

import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.service.FlightAssistantTools;
import se.lexicon.flightbooking_api.service.FlightBookingService;
import se.lexicon.flightbooking_api.service.OpenAiService;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class OpenAiController {

    private final OpenAiService openAiService;
    private final FlightBookingService flightBookingService;
    private final FlightAssistantTools flightAssistantTools;

    @Autowired
    public OpenAiController(OpenAiService openAiService, FlightBookingService flightBookingService, FlightAssistantTools flightAssistantTools) {
        this.openAiService = openAiService;
        this.flightBookingService = flightBookingService;
        this.flightAssistantTools = flightAssistantTools;
    }



    @GetMapping("/flight-assistant")
    public String flightAssistant(@RequestParam String message){
        String systemPrompt = """
                You are a flight booking assistant for fokkAirlines. Your job is to assist users with flight-related queries.
                You can help users find available flights and book them, but also find booked flights
                when the user provides their email adress. The last thing you can help them with is to
                cancel flights that they already have booked.
                Use the tools provided to you to assists the user.""";


        return openAiService.chatWithTools(systemPrompt, message,null, flightAssistantTools);
    }

    @PostMapping
    public String chatWithHistory(@RequestBody ChatRequest request) {
        String systemPrompt = """
            You are a flight booking assistant for fokkAirlines. Your job is to assist users with flight-related queries.
            You can help users find available flights and book them, but also find booked flights
            when the user provides their email address. The last thing you can help them with is to
            cancel flights that they already have booked.
            Use the tools provided to you to assist the user.

            When booking flights, ask for details step by step and remember the conversation context.""";

        return openAiService.chatWithTools(systemPrompt, request.getMessage(), request.getConversationHistory(), flightAssistantTools);
    }






}

package se.lexicon.flightbooking_api.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.lexicon.flightbooking_api.entity.Flight;
import se.lexicon.flightbooking_api.repository.FlightRepository;
import se.lexicon.flightbooking_api.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
public class ChatClientController {

    private final FlightService flightService;
    private final ChatClient chatClient;
    private final FlightRepository flightRepository;

    public ChatClientController(FlightService flightService, ChatClient chatClient, FlightRepository flightRepository) {
        this.flightService = flightService;
        this.chatClient = chatClient;
        this.flightRepository = flightRepository;

    }

    @GetMapping("/debug-flights")
    public String debugFlights() {
        List<Flight> flights = flightService.getAvailableFlights();
        return "Found " + flights.size() + " flights: " + flights.toString();
    }

    @GetMapping("/debug-all-flights")
    public String debugAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return "Found " + flights.size() + " total flights: " + flights.toString();
    }

    @GetMapping
    public String handleChatClientMessages(@RequestParam String message) {
        if ("list available flights".equalsIgnoreCase(message)) {
            List<Flight> availableFlights = flightService.getAvailableFlights();
            return chatClient.prompt()
                    .user("Here are the available flights:" + availableFlights)
                    .call()
                    .content();
        }
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }




}

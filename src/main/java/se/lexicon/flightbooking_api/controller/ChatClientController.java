package se.lexicon.flightbooking_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.lexicon.flightbooking_api.entity.Flight;
import se.lexicon.flightbooking_api.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
public class ChatClientController {

    private final FlightService flightService;


    public ChatClientController(FlightService flightService) {
        this.flightService = flightService;

    }

    @GetMapping
    public List<Flight> handleChatClientMessages(@RequestParam String message) {
        if ("list available flights".equalsIgnoreCase(message)) {
            return flightService.getAvailableFlights();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message format");
    }




}

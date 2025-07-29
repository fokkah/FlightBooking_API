package se.lexicon.flightbooking_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.ChatBotResponseDTO;
import se.lexicon.flightbooking_api.dto.ChatBotUserMessageDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.service.FlightBookingService;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatBotController {

    private final FlightBookingService flightBookingService;

    @PostMapping("/message")
    public ResponseEntity<ChatBotResponseDTO> handleMessage(@RequestBody ChatBotUserMessageDTO chatMessage) {
        String message = chatMessage.getMessage();
        String email = chatMessage.getEmail();
        String response;

        if (message.contains("availabe")) {

            List<AvailableFlightDTO> flights = flightBookingService.findAvailableFlights();
            response = "Available flights: " + flights.toString();


        } else if (message.contains("my bookings") || message.contains("my flights") && email != null) {
            List<FlightBookingDTO> bookings = flightBookingService.findBookingsByEmail(email);
            response = "This is what you have booked so far" + bookings.toString();

        } else if (message.contains("cancel") || message.contains("delete") && email != null) {
            Long flightId = extractFlightId(message);
            if (flightId != null) {
                flightBookingService.cancelFlight(flightId, email);
                response = "Flight" + flightId + "is now cancelled!";
            } else {
                response = "You need to specify a flight ID to cancel!";

            }

        } else {
            response = "I didnt really understand what you were asking for, can you be more specific?";
        }
        return ResponseEntity.ok(new ChatBotResponseDTO(response));
    }

    private Long extractFlightId(String message) {
        // Simple extraction logic, improve as needed
        String[] words = message.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[ i ].equals("flight") && i + 1 < words.length) {
                try {
                    return Long.parseLong(words[ i + 1 ]);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }
}



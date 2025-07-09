package se.lexicon.flightbooking_api.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ToolContext;

import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.repository.FlightBookingRepository;

import java.util.List;

@Service
public class AiTools {

    private final FlightBookingService flightBookingService;


    @Autowired
    public AiTools(FlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;

    }

    @Tool(description = "Fetch my booking details")
    public List<FlightBookingDTO> findBookingsByEmail(String email){
        List<FlightBookingDTO> bookings = flightBookingService.findBookingsByEmail(email);
        if (bookings == null || bookings.isEmpty()) {
            throw new IllegalArgumentException("No bookings found with that email");

        }


        return bookings;
    }

}

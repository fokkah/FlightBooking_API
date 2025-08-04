package se.lexicon.flightbooking_api.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.BookFlightRequestDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.dto.FlightListDTO;
import se.lexicon.flightbooking_api.entity.Flight;
import se.lexicon.flightbooking_api.entity.FlightBooking;
import se.lexicon.flightbooking_api.entity.FlightStatus;
import se.lexicon.flightbooking_api.repository.FlightBookingRepository;
import se.lexicon.flightbooking_api.repository.FlightRepository;

import java.util.List;


@Component
public class ToolCalling {

    private final FlightBookingRepository flightBookingRepository;
    private final FlightRepository flightRepository;
    private final FlightBookingService flightBookingService;
    private final FlightService flightService;

    public ToolCalling(FlightRepository flightRepository, FlightBookingRepository flightBookingRepository, FlightBookingService flightBookingService, FlightService flightService) {
        this.flightBookingRepository = flightBookingRepository;
        this.flightBookingService = flightBookingService;
        this.flightRepository = flightRepository;
        this.flightService = flightService;
    }

    @Tool(description = "Find booked flight by passenger email")
    public FlightBookingDTO findBookedFlightByEmail(String email) {
        FlightBooking bookedFlight = flightBookingRepository.findByPassengerEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No booked flight found for email: " + email));
        return toDto(bookedFlight);
    }

    private FlightBookingDTO toDto(FlightBooking bookedFlight) {
        return new FlightBookingDTO(
                bookedFlight.getId(),
                bookedFlight.getFlightNumber(),
                bookedFlight.getPassengerEmail(),
                bookedFlight.getPassengerName(),
                bookedFlight.getDepartureTime(),
                bookedFlight.getArrivalTime(),
                bookedFlight.getStatus().toString(),
                bookedFlight.getDestination(),
                bookedFlight.getPrice()
        );
    }


    @Tool(description = "Book a new flight")
    public FlightBookingDTO bookFlight(Long flightNumber, BookFlightRequestDTO bookingRequest) {
        return flightBookingService.bookFlight(flightNumber, bookingRequest);
    }


    @Tool(description = "Cancel a booked flight by email and flight ID")
    public FlightBookingDTO cancelBookedFlight(Long flightId, String passengerEmail) {
        FlightBooking flightBooking = flightBookingRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + flightId));

        if (!flightBooking.getPassengerEmail().equals(passengerEmail)) {
            throw new RuntimeException("Passenger email does not match the booking");

    }
        flightBooking.setStatus(FlightStatus.AVAILABLE);
        FlightBooking cancelledFlight = flightBookingRepository.save(flightBooking);
        return toDto(cancelledFlight);
    }

    @Tool(description = "Get available flights")
    public Object getAvailableFlights(String departure, String destination, String date){
        return flightService.getAvailableFlights(departure, destination, date);


    }


}


package se.lexicon.flightbooking_api.service;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.dto.FlightListDTO;

import java.util.List;

@Service
public class FlightAssistantTools {

    private final FlightBookingService flightBookingService;


    public FlightAssistantTools(FlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }


    @Tool(name = "getAvailableFlights", description = "Retrieve a list of available flights.")
    public List<AvailableFlightDTO> getAvailableFlights() {

        return flightBookingService.findAvailableFlights()
                .stream()
                .map(availableFlight -> new AvailableFlightDTO(
                        availableFlight.id(),
                        availableFlight.flightNumber(),
                        availableFlight.departureTime(),
                        availableFlight.arrivalTime(),
                        availableFlight.destination(),
                        availableFlight.price()

                ))
                .toList();
    }


    @Tool(name = "findBookedFlights", description = "Retrieve a list of booked flights for a specific passenger by their email address.")
    public List<FlightBookingDTO> getBookedFlights(String email) {
        return flightBookingService.findBookingsByEmail(email)
                .stream()
                .map(booking -> new FlightBookingDTO(
                        booking.id(),
                        booking.flightNumber(),
                        booking.passengerName(),
                        booking.passengerEmail(),
                        booking.departureTime(),
                        booking.arrivalTime(),
                        booking.status(),
                        booking.destination(),
                        booking.price()
                ))
                .toList();
    }

    @Tool(name = "cancelFlight", description = "Cancel a booked flight by providing the flight ID and passenger email.")
    public String cancelFlight(Long flightId, String email) {
        try {
            List<FlightBookingDTO> bookingsByEmail = getBookedFlights(email);
            boolean bookingExists = bookingsByEmail.stream()
                    .anyMatch(booking -> booking.id().equals(flightId));
            if (bookingExists) {
                flightBookingService.cancelFlight(flightId, email);
                return "Flight with ID " + flightId + " has been cancelled successfully.";
            } else {
                return "No booking found for the provided flight ID and email.";
            }
        } catch (Exception e) {
            return "An error occurred while trying to cancel the flight: " + e.getMessage();
        }
    }
}

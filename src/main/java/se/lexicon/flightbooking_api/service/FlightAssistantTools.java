package se.lexicon.flightbooking_api.service;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.BookFlightRequestDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.dto.FlightListDTO;

import java.util.List;

@Service
public class FlightAssistantTools {

    private final FlightBookingService flightBookingService;


    public FlightAssistantTools(FlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }


    @Tool(name = "bookFlight", description = """
             You help the user to guide them through the process of booking a flight""")


    public String bookFlight(Long flightId, String passengerName, String passengerEmail) {
        try {
            BookFlightRequestDTO bookingRequest = new BookFlightRequestDTO(passengerName, passengerEmail);
            FlightBookingDTO bookedFlight = flightBookingService.bookFlight(flightId, bookingRequest);
            return "Flight booked successfully! Booking details: " +
                    "Booking ID: " + bookedFlight.id() +
                    ", Flight: " + bookedFlight.flightNumber() +
                    ", Passenger: " + bookedFlight.passengerName() +
                    ", Email: " + bookedFlight.passengerEmail();
        } catch (Exception e) {
            return "An error occurred while trying to book the flight: " + e.getMessage();
        }
    }

    @Tool(name = "cancelFlight", description = """
            You help the user to guide them through the process of cancelling a flight.
            """)
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

    @Tool(name = "getAvailableFlights", description = """
           You help the user to guide them through the process of finding available flights.""")


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


    @Tool(name = "findBookedFlights", description = "Show a list of booked flights. Enter email to see the booked flights.")
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

}

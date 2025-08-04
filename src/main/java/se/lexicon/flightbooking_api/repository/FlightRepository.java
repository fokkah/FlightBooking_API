package se.lexicon.flightbooking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.flightbooking_api.dto.FlightListDTO;
import se.lexicon.flightbooking_api.entity.Flight;
import se.lexicon.flightbooking_api.entity.FlightBooking;
import se.lexicon.flightbooking_api.entity.FlightStatus;

import java.util.List;

public interface FlightRepository  extends JpaRepository<Flight, Long> {

    List<Flight> findByStatus(FlightStatus status);

    // Additional query methods can be defined here if needed
    // For example, to find flights by destination or status, etc.

    // Example: List<FlightListDTO> findByDestination(String destination);
    // Example: List<FlightListDTO> findByStatus(String status);
}

package se.lexicon.flightbooking_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.dto.FlightListDTO;
import se.lexicon.flightbooking_api.entity.Flight;
import se.lexicon.flightbooking_api.entity.FlightBooking;
import se.lexicon.flightbooking_api.entity.FlightStatus;
import se.lexicon.flightbooking_api.repository.FlightBookingRepository;
import se.lexicon.flightbooking_api.repository.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceIMPL implements FlightService {
    @Override
    public List<Flight> getAvailableFlights() {
        return flightRepository.findByStatus(FlightStatus.AVAILABLE);
    }

    private final FlightRepository flightRepository;
    private final FlightBookingRepository flightBookingRepository;


    @Override
    public List<AvailableFlightDTO> getAvailableFlights(String departure, String destination, String date) {
        return flightRepository.findByStatus(FlightStatus.AVAILABLE)
                .stream()
                .filter(flight -> (departure == null || flight.getDeparture().equalsIgnoreCase(departure)) &&
                        (destination == null || flight.getDestination().equalsIgnoreCase(destination)) &&
                        (date == null || flight.getDepartureTime().toLocalDate().toString().equals(date)))
                .map(flight -> new AvailableFlightDTO(
                        flight.getId(),
                        flight.getFlightNumber(),
                        flight.getDepartureTime(),
                        flight.getArrivalTime(),
                        flight.getDestination(),
                        flight.getPrice()
                ))
                .collect(Collectors.toList());
    }
}

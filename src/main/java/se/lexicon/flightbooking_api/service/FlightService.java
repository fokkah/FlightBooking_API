package se.lexicon.flightbooking_api.service;

import org.springframework.stereotype.Service;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.entity.Flight;


import java.util.List;

@Service
public interface FlightService {



    List<AvailableFlightDTO> getAvailableFlights(String departure, String destination, String date);

    List<Flight> getAvailableFlights();
}

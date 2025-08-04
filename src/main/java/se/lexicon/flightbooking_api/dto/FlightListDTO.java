package se.lexicon.flightbooking_api.dto;

import java.time.LocalDateTime;

public record FlightListDTO(
    Long id,
    String flightNumber,
    String departure,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    String status,
    String destination,
    Double price
) {}
package se.lexicon.flightbooking_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightBookingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightBookingApiApplication.class, args);

        System.out.println("OPENAI_API_KEY: " + System.getenv("OPENAI_API_KEY"));
        
    }

}

package se.lexicon.flightbooking_api.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.service.FlightAssistantTools;
import se.lexicon.flightbooking_api.service.FlightBookingService;
import se.lexicon.flightbooking_api.service.OpenAiService;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class OpenAiController {

    private final OpenAiService openAiService;
    private final FlightBookingService flightBookingService;
    private final FlightAssistantTools flightAssistantTools;

    @Autowired
    public OpenAiController(OpenAiService openAiService, FlightBookingService flightBookingService, FlightAssistantTools flightAssistantTools) {
        this.openAiService = openAiService;
        this.flightBookingService = flightBookingService;
        this.flightAssistantTools = flightAssistantTools;
    }



    @GetMapping("/flight-assistant")
    public String flightAssistant(@RequestParam String message){
        String systemPrompt = """
                You are a flight booking assistant.
                You can help users find available flights and book them, but also find booked flights
                when the user provides their email adress. The last thing you can help them with is to
                cancel flights that they already have booked.
                Use the tools provided to you to assists the user.""";


        return openAiService.chatWithTools(systemPrompt, message, flightAssistantTools);
    }






}

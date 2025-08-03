package se.lexicon.flightbooking_api.controller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.flightbooking_api.service.OpenAiService;
import se.lexicon.flightbooking_api.service.OpenAiServiceIMPL;

@RestController
@RequestMapping("/api/chat")
public class OpenAiController {

    private final OpenAiServiceIMPL openAiServiceIMPL;

    @Autowired
    public OpenAiController(OpenAiService openAiService, OpenAiServiceIMPL openAiServiceIMPL) {
        this.openAiService = openAiService;
        this.openAiServiceIMPL = openAiServiceIMPL;
    }

    private final OpenAiService openAiService;

    @GetMapping
    public String welcome() {
        return "Welcome to FlightBooking API";
    }

    @GetMapping("/messages")
    public String chatWithAi(
            @NotNull(message = "Question cant be null.")
            @NotBlank(message = "Question cant be blank.")
            @Size(max = 300)
            @RequestParam String question
    ) {
        return openAiService.chatMessageToAi(question);
    }





}

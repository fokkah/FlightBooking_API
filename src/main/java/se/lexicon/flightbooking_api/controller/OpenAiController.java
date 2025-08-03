package se.lexicon.flightbooking_api.controller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
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

    @GetMapping("/messages/stream")
    public Flux<String> chatWithAiAsStream(
            @NotNull(message = "Question cant be null.")
            @NotBlank(message = "Question cant be blank.")
            @Size(max = 300)
            @RequestParam String question
    ) {
        return openAiService.chatMessageAsStreamToAi(question);


    }

    @GetMapping("/messages/fokkBot")
    public String chatWithAiWithInstructions(
            @NotNull(message = "Question cant be null.")
            @NotBlank(message = "Question cant be blank.")
            @Size(max = 300)
            @RequestParam String question
    ) {
        return openAiService.chatMessageToAiWithInstruction(question);
    }

    @GetMapping("/messages/memory")
    public String chatWithAiMemory(
            @NotNull(message = "Conversation ID cant be null.")
            @NotBlank(message = "Conversation ID cant be blank.")
            @Size(max = 50)
            @RequestParam String conversationId,
            @NotNull(message = "Conversation ID cant be null.")
            @NotBlank(message = "Conversation ID cant be blank.")
            @Size(max = 300)
            @RequestParam String message){
        System.out.println("Conversation ID: " + conversationId);
        System.out.println("Message: " + message);
        return openAiService.chatMemory(message, conversationId);

    }


}

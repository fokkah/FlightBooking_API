package se.lexicon.flightbooking_api.controller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import se.lexicon.flightbooking_api.service.FlightService;
import se.lexicon.flightbooking_api.service.OpenAiService;
import se.lexicon.flightbooking_api.service.OpenAiServiceIMPL;

@RestController
@RequestMapping("/api/chat")
public class OpenAiController {


    private final OpenAiService openAiService;

    private final OpenAiServiceIMPL openAiServiceIMPL;
    private final FlightService flightService;
    private final ChatClient chatClient;

    @Autowired
    public OpenAiController(OpenAiService openAiService, OpenAiServiceIMPL openAiServiceIMPL, FlightService flightService, ChatClient chatClient) {
        this.openAiService = openAiService;
        this.openAiServiceIMPL = openAiServiceIMPL;
        this.flightService = flightService;
        this.chatClient = chatClient;
    }



    @GetMapping
    public String welcome() {
        return "Welcome to FlightBooking API";
    }

    @PostMapping("/messages")
    public String chatWithAi(
            @NotNull(message = "Question cant be null.")
            @NotBlank(message = "Question cant be blank.")
            @Size(max = 300)
            @RequestParam String question
    ) {
        return openAiService.chatMessageToAi(question);
    }

    @PostMapping("/messages/stream")
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

    @PostMapping("/messages/memory")
    public Object chatWithAiMemory(
            @NotNull(message = "Conversation ID cant be null.")
            @NotBlank(message = "Conversation ID cant be blank.")
            @Size(max = 50)
            @RequestParam String conversationId,
            @NotNull(message = "Conversation ID cant be null.")
            @NotBlank(message = "Conversation ID cant be blank.")
            @Size(max = 300)
            @RequestParam String message){

        if ("list available flights".equalsIgnoreCase(message.trim())) {
            return flightService.getAvailableFlights(null, null, null);
        }
        System.out.println("Conversation ID: " + conversationId);
        System.out.println("Message: " + message);
        return openAiService.chatMemory(message, conversationId);

    }

    @PostMapping("/messages/tool")
    public Object chatWithAiTool(
            @RequestParam String message){
        if ("list available flights".equalsIgnoreCase(message.trim())) {
            return flightService.getAvailableFlights(null, null, null);
        }
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}

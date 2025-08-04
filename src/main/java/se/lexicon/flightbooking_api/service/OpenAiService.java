package se.lexicon.flightbooking_api.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final ChatModel chatModel;

    public OpenAiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chatWithTools(String systemPrompt, String userMessage, FlightAssistantTools tools) {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .tools(tools)
                .call()
                .content();

    }








}

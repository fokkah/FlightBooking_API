package se.lexicon.flightbooking_api.service;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class OpenAiServiceIMPL  implements OpenAiService {


    private final OpenAiChatModel openAiChatModel;
    private final ChatMemory chatMemory;
    private final ToolCalling toolCalling;


    public OpenAiServiceIMPL(OpenAiChatModel openAiChatModel, ChatMemory chatMemory, ToolCalling toolCalling) {
        this.openAiChatModel = openAiChatModel;
        this.chatMemory = chatMemory;
        this.toolCalling = toolCalling;
    }

    @Override
    public String chatMessageToAi(final String message) {

        return openAiChatModel.call(message);
    }

    @Override
    public Flux<String> chatMessageAsStreamToAi(final String message) {

        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        try {
            String chatMessageFromAi = openAiChatModel.call(message);

            return openAiChatModel.stream(message);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error in chatMessageToAi" + e.getMessage(), e);

        }
    }

    @Override
    public String chatMessageToAiWithInstruction(String message) {

        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        SystemMessage systemMessage = SystemMessage.builder().text("""
                        You are a helpfull and professional assistent working on "fokkAirlines" and youre name is fokkBot, 
                        that helps users with searching, booking and cancelling flights.
                        """)
                .build();
        UserMessage userMessage = UserMessage.builder().text(message).build();

        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .chatOptions(ChatOptions.builder()
                        .model("gpt-4.1-mini")
                        .temperature(0.9)
                        .build())
                .build();
        ChatResponse chatResponse = openAiChatModel.call(prompt);

        return chatResponse.getResult() != null ? chatResponse.getResult().getOutput().getText() : "Does not compute";
    }

    /*
    @Override
    public String chatMemory(final String message,final String conversationId) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (conversationId == null || conversationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Conversation ID cannot be null or empty");
        }
        UserMessage userMessage = UserMessage.builder().
                text(message)
                .build();

        chatMemory.add(conversationId, userMessage);
        Prompt prompt = Prompt.builder()
                .messages(chatMemory.get(conversationId))
                .build();
        ChatResponse chatResponse =  openAiChatModel.call(prompt);
        chatMemory.add(conversationId, chatResponse.getResult().getOutput());
        chatMemory.get(conversationId).forEach(msg -> System.out.println(msg.getText()));

        System.out.println("Message in Memory" + chatMemory.get(conversationId));
        System.out.println("Memory size: " + chatMemory.get(conversationId).size());

        return chatResponse.getResult().getOutput().getText();
    }
     */
    @Override
    public String chatMemory(String message, String conversationId) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (conversationId == null || conversationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Conversation ID cannot be null or empty");
        }
        UserMessage userMessage = UserMessage.builder().text(message).build();
        chatMemory.add(conversationId, userMessage);

        Prompt prompt = Prompt.builder()
                .messages(chatMemory.get(conversationId))
                .build();

        ChatResponse chatResponse = openAiChatModel.call(prompt);
        chatMemory.add(conversationId, chatResponse.getResult().getOutput());
        return chatResponse.getResult().getOutput().getText();

    }
}


package se.lexicon.flightbooking_api.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import se.lexicon.flightbooking_api.dto.ChatDTO.ChatMessage;
import se.lexicon.flightbooking_api.service.FlightAssistantTools;



import java.util.List;

@Service
public class OpenAiService {

    private final ChatModel chatModel;
    private static final int MAX_HISTORY_SIZE = 20;

    public OpenAiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }


    public String chatWithTools(String systemPrompt, String userMessage, List<ChatMessage> conversationHistory, FlightAssistantTools tools) {
        ChatClient chatClient = ChatClient.builder(chatModel).build();

        List<ChatMessage> trimmedHistory = limitHistorySize(conversationHistory, MAX_HISTORY_SIZE);

        System.out.println("=== DEBUG CONVERSATION HISTORY ===");
        System.out.println("OG History size: " + (conversationHistory != null ? conversationHistory.size() : "null"));
        System.out.println("Trimmed history size: " + (trimmedHistory != null ? trimmedHistory.size() : "null"));


        StringBuilder fullConversation = new StringBuilder();

        if (trimmedHistory != null && !trimmedHistory.isEmpty()) {
            for (ChatMessage chatMessage : trimmedHistory) {
                if ("user".equals(chatMessage.getRole())) {
                    fullConversation.append("User: ").append(chatMessage.getContent()).append("\n");
                } else if ("assistant".equals(chatMessage.getRole())) {
                    fullConversation.append("Assistant: ").append(chatMessage.getContent()).append("\n");
                }
            }
            fullConversation.append("\n");
        }else {
            System.out.println("No conversation history provided, starting fresh.");
        }
        fullConversation.append("Current User Message: ").append(userMessage);

        String enhancedHistorySystemPrompt = systemPrompt + "\n\nCRUCIAL: U must remember information from previous messages in this conversation. You use the information to be a better assistant.";

        return chatClient.prompt()
                .system(enhancedHistorySystemPrompt)
                .user(fullConversation.toString())
                .tools(tools)
                .call()
                .content();
    }

    private List<ChatMessage> limitHistorySize(List<ChatMessage> history, int maxMessages) {

        if (history == null || history.size() <= maxMessages) {
            return history;
        }

        return history.subList(history.size() - maxMessages, history.size());
    }

}

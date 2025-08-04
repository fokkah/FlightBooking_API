package se.lexicon.flightbooking_api.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.lexicon.flightbooking_api.service.OpenAiService;

@Configuration
public class ChatClientCFG {

    @Bean
    public ChatClient chatClient() {
        OpenAiChatModel chatModel = OpenAiChatModel.builder().build();
        return ChatClient.builder(chatModel).build();
    }
}
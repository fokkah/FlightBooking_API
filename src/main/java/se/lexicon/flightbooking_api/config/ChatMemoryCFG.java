package se.lexicon.flightbooking_api.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("se.lexicon.*")
public class ChatMemoryCFG {

    @Bean
    public ChatMemory chatMemory(){


        return MessageWindowChatMemory.builder()
                .maxMessages(50).build();

    }


}

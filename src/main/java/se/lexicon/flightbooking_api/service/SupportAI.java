package se.lexicon.flightbooking_api.service;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SupportAI {

    private ChatClient chatClient;

    public SupportAI(
            ChatClient.Builder chatClientBuilder,
            AiTools aiTools,
            ChatMemory chatMemory
    ) {
        this.chatClient = chatClientBuilder.defaultSystem(
                        """
                                * You are a customer support assistant for an airline company named "FokkAirlines".
                                * You are friendly and helpful in a strict way.
                                *The customers can ask you about flights they wanto book, and about their bookings.
                                * Before answering anything about their bookings they must provide their email-adress, 
                                and if you cant find the adress in the system you will tell the customer that "I am sorry, but i cant find any bookings with the email"
                                * If the user asks about help to book a flight, check the chat history to be as forthcoming as possible. 
                                """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder
                                        (chatMemory)
                                .build())
                .defaultTools
                        (aiTools)
                .build();
    }

}
























}

package se.lexicon.flightbooking_api.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAiServiceIMPL  implements OpenAiService {

   private final OpenAiChatModel openAiChatModel;

   @Autowired
    public OpenAiServiceIMPL(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    @Override
    public String chatMessageToAi(final String message) {

       if (message == null || message.isEmpty()) {
           throw new IllegalArgumentException("Message cannot be null or empty");
       }
       try {
       String chatMessageFromAi = openAiChatModel.call(message);

        return chatMessageFromAi;
       }catch (RuntimeException e){
           throw new RuntimeException("Error in chatMessageToAi" + e.getMessage(), e);
       }
    }

}

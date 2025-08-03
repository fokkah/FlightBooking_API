package se.lexicon.flightbooking_api.service;

import reactor.core.publisher.Flux;

public interface OpenAiService {

    //String chatMessageToAi(String message);
    String chatMessageToAi(String message);

    Flux<String> chatMessageAsStreamToAi(String message);

    String chatMessageToAiWithInstruction(String message);

    String chatMemory( String message, String conversation);






}

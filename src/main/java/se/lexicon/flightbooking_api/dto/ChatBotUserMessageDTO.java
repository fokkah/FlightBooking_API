package se.lexicon.flightbooking_api.dto;

import lombok.Data;

@Data
public class ChatBotUserMessageDTO {
    private String message;
    private String email;
    private String passengerName;

}

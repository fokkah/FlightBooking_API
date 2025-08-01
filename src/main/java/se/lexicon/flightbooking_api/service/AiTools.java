package se.lexicon.flightbooking_api.service;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.chat.messages.AssistantMessage;

public class AiTools {

    private final String tool;
    private final JsonNode params;

    public AiTools(String tool, JsonNode params) {
        this.tool = tool;
        this.params = params;
    }

    public String getTool() {
        return tool;
    }

    public JsonNode getParams() {
        return params;
    }


}

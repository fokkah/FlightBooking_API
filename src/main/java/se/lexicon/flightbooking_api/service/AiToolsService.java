package se.lexicon.flightbooking_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.Generation;


import java.util.Optional;

public class AiToolsService {
    public static Optional<AiTools> parse(Generation response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String content = response.getOutput().getContent();
            JsonNode node = mapper.readTree(content);
                if (node.has("tool")) {
                    String tool = node.get("tool").asText();
                    JsonNode params = node.get("params");
                    return Optional.of(new AiTools(tool, params));
                }
        } catch (Exception ignored) {}
        return Optional.empty();

        }
    }




















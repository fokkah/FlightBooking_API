package se.lexicon.flightbooking_api.service;


import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatHistoryService {
    private static final int MAX_HISTORY = 20;
    private final Map<String, Deque<Map<String, String>>> historyMap = new ConcurrentHashMap<>();

    public void addMessage(String userId, String role, String content) {
        historyMap.putIfAbsent(userId, new ArrayDeque<>());
        Deque<Map<String, String>> history = historyMap.get(userId);
        if (history.size() >= MAX_HISTORY) history.pollFirst();
        history.addLast(Map.of("role", role, "content", content));
    }

    public List<Map<String, String>> getHistory(String userId) {
        return new ArrayList<>(historyMap.getOrDefault(userId, new ArrayDeque<>()));

    }

    public void clearHistory(String userId) {
        historyMap.remove(userId);
    }


}

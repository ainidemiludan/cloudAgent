package com.company.cloudagent.infra.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaAgentEventPublisher implements AgentEventPublisher {

    private static final String TOPIC = "agent.chat.completed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishCompleted(String sessionId, String userId, String prompt, String answer) {
        Map<String, Object> event = new HashMap<>();
        event.put("sessionId", sessionId);
        event.put("userId", userId);
        event.put("prompt", prompt);
        event.put("answer", answer);
        event.put("timestamp", Instant.now().toString());
        kafkaTemplate.send(TOPIC, sessionId, event);
    }
}

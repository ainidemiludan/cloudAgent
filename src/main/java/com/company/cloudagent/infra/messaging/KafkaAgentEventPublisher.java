package com.company.cloudagent.infra.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaAgentEventPublisher implements AgentEventPublisher {

    private static final String TOPIC = "agent.chat.completed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishCompleted(String sessionId, String userId, String prompt, String answer) {
        Map<String, Object> event = Map.of(
                "sessionId", sessionId,
                "userId", userId,
                "prompt", prompt,
                "answer", answer,
                "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, sessionId, event);
    }
}

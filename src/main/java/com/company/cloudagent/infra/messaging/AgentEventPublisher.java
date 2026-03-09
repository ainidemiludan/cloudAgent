package com.company.cloudagent.infra.messaging;

public interface AgentEventPublisher {

    void publishCompleted(String sessionId, String userId, String prompt, String answer);
}

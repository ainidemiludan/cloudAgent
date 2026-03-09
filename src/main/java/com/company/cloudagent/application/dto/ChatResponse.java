package com.company.cloudagent.application.dto;

import java.util.List;

public record ChatResponse(
        String sessionId,
        String answer,
        List<String> skillsExecuted,
        List<String> retrievedKnowledge
) {
}

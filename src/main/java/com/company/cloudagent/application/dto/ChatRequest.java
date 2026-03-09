package com.company.cloudagent.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ChatRequest(
        @NotBlank String userId,
        @NotBlank String sessionId,
        @NotBlank String prompt,
        List<String> skillNames
) {
}

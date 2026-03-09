package com.company.cloudagent.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ChatRequest(
        @NotBlank String userId,
        @NotBlank String sessionId,
        @NotBlank String prompt,
        @NotEmpty List<String> skillNames
) {
}

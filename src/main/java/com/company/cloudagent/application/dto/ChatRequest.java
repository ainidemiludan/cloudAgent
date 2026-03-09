package com.company.cloudagent.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String sessionId;

    @NotBlank
    private String prompt;

    private List<String> skillNames;
}

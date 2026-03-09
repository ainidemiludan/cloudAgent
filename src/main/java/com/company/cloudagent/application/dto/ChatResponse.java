package com.company.cloudagent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String sessionId;
    private String answer;
    private List<String> skillsExecuted;
    private List<String> retrievedKnowledge;
}

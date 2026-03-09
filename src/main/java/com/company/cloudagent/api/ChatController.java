package com.company.cloudagent.api;

import com.company.cloudagent.application.ChatOrchestrationService;
import com.company.cloudagent.application.dto.ChatRequest;
import com.company.cloudagent.application.dto.ChatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatOrchestrationService chatOrchestrationService;

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        log.info("Incoming chat request: sessionId={}, userId={}, skills={}",
                request.sessionId(), request.userId(), request.skillNames());
        return chatOrchestrationService.handle(request);
    }
}

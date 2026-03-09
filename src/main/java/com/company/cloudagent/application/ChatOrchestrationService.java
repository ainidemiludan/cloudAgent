package com.company.cloudagent.application;

import com.company.cloudagent.application.dto.ChatRequest;
import com.company.cloudagent.application.dto.ChatResponse;
import com.company.cloudagent.domain.SkillExecutor;
import com.company.cloudagent.domain.SkillMatcher;
import com.company.cloudagent.infra.messaging.AgentEventPublisher;
import com.company.cloudagent.infra.persistence.ConversationMapper;
import com.company.cloudagent.infra.persistence.ConversationRecord;
import com.company.cloudagent.infra.vector.VectorKnowledgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatOrchestrationService {

    private final DashscopeGraphWorkflowService dashscopeGraphWorkflowService;
    private final VectorKnowledgeService vectorKnowledgeService;
    private final SkillExecutor skillExecutor;
    private final SkillMatcher skillMatcher;
    private final AgentEventPublisher agentEventPublisher;
    private final ConversationMapper conversationMapper;

    @Value("${cloud-agent.mock.enabled:true}")
    private boolean mockEnabled;

    public ChatResponse handle(ChatRequest request) {
        log.debug("Start orchestration for sessionId={} promptLength={}", request.getSessionId(), request.getPrompt().length());

        List<String> knowledge = mockEnabled
                ? List.of("[mock] 企业知识库已启用模拟数据，可先验证对话主链路")
                : vectorKnowledgeService.retrieve(request.getPrompt());
        log.debug("Knowledge retrieved: sessionId={} size={} mockEnabled={}", request.getSessionId(), knowledge.size(), mockEnabled);

        List<String> selectedSkills = request.getSkillNames() == null || request.getSkillNames().isEmpty()
                ? skillMatcher.match(request.getPrompt(), knowledge)
                : request.getSkillNames();
        log.debug("Skills selected: sessionId={} skills={}", request.getSessionId(), selectedSkills);

        List<String> executed = skillExecutor.execute(selectedSkills, request.getPrompt(), knowledge);
        log.debug("Skills executed: sessionId={} skills={}", request.getSessionId(), executed);

        String answer = mockEnabled
                ? "[mock-answer] 已完成需求解析：" + request.getPrompt() + "；系统自动选择技能=" + executed
                : dashscopeGraphWorkflowService.generateAnswer(request.getPrompt(), knowledge, executed);

        if (mockEnabled) {
            log.info("Mock mode enabled, skip MySQL/Kafka IO for sessionId={}", request.getSessionId());
        } else {
            conversationMapper.insert(ConversationRecord.of(request.getSessionId(), request.getUserId(), request.getPrompt(), answer));
            agentEventPublisher.publishCompleted(request.getSessionId(), request.getUserId(), request.getPrompt(), answer);
        }

        log.info("Chat completed: sessionId={}, userId={}, answerLength={}",
                request.getSessionId(), request.getUserId(), answer == null ? 0 : answer.length());

        return new ChatResponse(request.getSessionId(), answer, executed, knowledge);
    }
}

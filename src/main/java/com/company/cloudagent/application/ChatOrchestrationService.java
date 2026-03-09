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
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatOrchestrationService {

    private final ChatClient chatClient;
    private final VectorKnowledgeService vectorKnowledgeService;
    private final SkillExecutor skillExecutor;
    private final SkillMatcher skillMatcher;
    private final AgentEventPublisher agentEventPublisher;
    private final ConversationMapper conversationMapper;

    @Value("${cloud-agent.mock.enabled:true}")
    private boolean mockEnabled;

    public ChatResponse handle(ChatRequest request) {
        log.debug("Start orchestration for sessionId={} promptLength={}", request.sessionId(), request.prompt().length());

        List<String> knowledge = mockEnabled
                ? List.of("[mock] 企业知识库已启用模拟数据，可先验证对话主链路")
                : vectorKnowledgeService.retrieve(request.prompt());
        log.debug("Knowledge retrieved: sessionId={} size={} mockEnabled={}", request.sessionId(), knowledge.size(), mockEnabled);

        List<String> selectedSkills = request.skillNames() == null || request.skillNames().isEmpty()
                ? skillMatcher.match(request.prompt(), knowledge)
                : request.skillNames();
        log.debug("Skills selected: sessionId={} skills={}", request.sessionId(), selectedSkills);

        List<String> executed = skillExecutor.execute(selectedSkills, request.prompt(), knowledge);
        log.debug("Skills executed: sessionId={} skills={}", request.sessionId(), executed);

        String finalPrompt = """
                你是企业内部云端智能体，请基于以下上下文回复：
                用户问题：%s
                检索知识：%s
                已执行技能：%s
                """.formatted(request.prompt(), knowledge, executed);

        String answer = mockEnabled
                ? "[mock-answer] 已完成需求解析：" + request.prompt() + "；系统自动选择技能=" + executed
                : chatClient.prompt(finalPrompt).call().content();

        if (mockEnabled) {
            log.info("Mock mode enabled, skip MySQL/Kafka IO for sessionId={}", request.sessionId());
        } else {
            conversationMapper.insert(ConversationRecord.of(request.sessionId(), request.userId(), request.prompt(), answer));
            agentEventPublisher.publishCompleted(request.sessionId(), request.userId(), request.prompt(), answer);
        }

        log.info("Chat completed: sessionId={}, userId={}, answerLength={}",
                request.sessionId(), request.userId(), answer == null ? 0 : answer.length());

        return new ChatResponse(request.sessionId(), answer, executed, knowledge);
    }
}

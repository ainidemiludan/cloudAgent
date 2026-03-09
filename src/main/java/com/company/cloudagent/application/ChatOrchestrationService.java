package com.company.cloudagent.application;

import com.company.cloudagent.application.dto.ChatRequest;
import com.company.cloudagent.application.dto.ChatResponse;
import com.company.cloudagent.domain.SkillExecutor;
import com.company.cloudagent.infra.messaging.AgentEventPublisher;
import com.company.cloudagent.infra.persistence.ConversationMapper;
import com.company.cloudagent.infra.persistence.ConversationRecord;
import com.company.cloudagent.infra.vector.VectorKnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatOrchestrationService {

    private final ChatClient chatClient;
    private final VectorKnowledgeService vectorKnowledgeService;
    private final SkillExecutor skillExecutor;
    private final AgentEventPublisher agentEventPublisher;
    private final ConversationMapper conversationMapper;

    public ChatResponse handle(ChatRequest request) {
        List<String> knowledge = vectorKnowledgeService.retrieve(request.prompt());
        List<String> executed = skillExecutor.execute(request.skillNames(), request.prompt(), knowledge);

        String finalPrompt = """
                你是企业内部云端智能体，请基于以下上下文回复：
                用户问题：%s
                检索知识：%s
                已执行技能：%s
                """.formatted(request.prompt(), knowledge, executed);

        String answer = chatClient.prompt(finalPrompt).call().content();
        conversationMapper.insert(ConversationRecord.of(request.sessionId(), request.userId(), request.prompt(), answer));
        agentEventPublisher.publishCompleted(request.sessionId(), request.userId(), request.prompt(), answer);

        return new ChatResponse(request.sessionId(), answer, executed, knowledge);
    }
}

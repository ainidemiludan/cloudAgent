package com.company.cloudagent;

import com.company.cloudagent.application.ChatOrchestrationService;
import com.company.cloudagent.application.DashscopeGraphWorkflowService;
import com.company.cloudagent.application.dto.ChatRequest;
import com.company.cloudagent.application.dto.ChatResponse;
import com.company.cloudagent.domain.DefaultSkillExecutor;
import com.company.cloudagent.domain.DefaultSkillMatcher;
import com.company.cloudagent.infra.messaging.AgentEventPublisher;
import com.company.cloudagent.infra.persistence.ConversationMapper;
import com.company.cloudagent.infra.vector.VectorKnowledgeService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class ChatOrchestrationServiceMockModeTest {

    @Test
    void shouldRunFullFlowWithMockDataAndSkipInfraIo() {
        VectorKnowledgeService vectorKnowledgeService = mock(VectorKnowledgeService.class);
        AgentEventPublisher agentEventPublisher = mock(AgentEventPublisher.class);
        ConversationMapper conversationMapper = mock(ConversationMapper.class);

        ChatOrchestrationService service = new ChatOrchestrationService(
                mock(DashscopeGraphWorkflowService.class),
                vectorKnowledgeService,
                new DefaultSkillExecutor(),
                new DefaultSkillMatcher(),
                agentEventPublisher,
                conversationMapper
        );
        ReflectionTestUtils.setField(service, "mockEnabled", true);

        ChatResponse response = service.handle(new ChatRequest("u-001", "s-001", "请总结本周任务", null));

        assertTrue(response.getAnswer().startsWith("[mock-answer]"));
        assertTrue(response.getRetrievedKnowledge().get(0).contains("[mock]"));
        verifyNoInteractions(vectorKnowledgeService, agentEventPublisher, conversationMapper);
    }
}

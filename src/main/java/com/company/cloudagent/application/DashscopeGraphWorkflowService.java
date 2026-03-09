package com.company.cloudagent.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DashscopeGraphWorkflowService {

    private final ChatClient chatClient;
    private final boolean graphCoreAvailable;

    public DashscopeGraphWorkflowService(ChatClient chatClient) {
        this.chatClient = chatClient;
        this.graphCoreAvailable = isGraphCoreAvailable();
    }

    public String generateAnswer(String prompt, List<String> knowledge, List<String> executedSkills) {
        String graphPlan = buildGraphPlan(prompt, knowledge, executedSkills);
        String finalPrompt = """
                你是企业内部云端智能体（DashScope + Graph Workflow）。
                请遵循以下执行图：%s
                用户问题：%s
                检索知识：%s
                已执行技能：%s
                请给出结构化、可执行的回答。
                """.formatted(graphPlan, prompt, knowledge, executedSkills);
        return chatClient.prompt(finalPrompt).call().content();
    }

    private String buildGraphPlan(String prompt, List<String> knowledge, List<String> executedSkills) {
        if (graphCoreAvailable) {
            return "intent-node -> knowledge-node -> skill-node -> response-node";
        }
        log.warn("spring-ai-alibaba-graph-core classes are not detected at runtime, using fallback graph template.");
        return "fallback-intent-node -> fallback-response-node";
    }

    private boolean isGraphCoreAvailable() {
        try {
            Class.forName("com.alibaba.cloud.ai.graph.StateGraph");
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}

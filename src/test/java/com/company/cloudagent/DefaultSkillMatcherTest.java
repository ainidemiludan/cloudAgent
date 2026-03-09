package com.company.cloudagent;

import com.company.cloudagent.domain.DefaultSkillMatcher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSkillMatcherTest {

    private final DefaultSkillMatcher skillMatcher = new DefaultSkillMatcher();

    @Test
    void shouldMatchKnowledgeAndSummarySkillsByPrompt() {
        List<String> matched = skillMatcher.match("请检索知识库并总结结果", List.of("k1"));

        assertEquals(List.of("knowledge-retrieval", "summarization"), matched);
    }

    @Test
    void shouldFallbackToGeneralAssistantWhenNoKeywordMatched() {
        List<String> matched = skillMatcher.match("你好", List.of());

        assertEquals(List.of("general-assistant"), matched);
    }
}

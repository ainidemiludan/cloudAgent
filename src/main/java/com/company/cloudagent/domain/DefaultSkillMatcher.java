package com.company.cloudagent.domain;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class DefaultSkillMatcher implements SkillMatcher {

    private static final Map<String, List<String>> RULES = Map.of(
            "knowledge-retrieval", List.of("知识", "文档", "检索", "查询", "search", "retrieve", "rag"),
            "summarization", List.of("总结", "摘要", "提炼", "summary", "summarize"),
            "task-planning", List.of("计划", "待办", "任务", "排期", "plan", "todo"),
            "risk-review", List.of("风险", "合规", "审计", "review", "compliance")
    );

    @Override
    public List<String> match(String prompt, List<String> knowledge) {
        String normalizedPrompt = normalize(prompt);
        String normalizedKnowledge = normalize(String.join(" ", knowledge));

        Map<String, Integer> scored = new LinkedHashMap<>();
        RULES.forEach((skill, keywords) -> scored.put(skill, score(keywords, normalizedPrompt, normalizedKnowledge)));

        List<String> ranked = scored.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        return ranked.isEmpty() ? List.of("general-assistant") : ranked;
    }

    private int score(List<String> keywords, String normalizedPrompt, String normalizedKnowledge) {
        int score = 0;
        for (String keyword : keywords) {
            String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
            if (normalizedPrompt.contains(normalizedKeyword)) {
                score += 2;
            }
            if (normalizedKnowledge.contains(normalizedKeyword)) {
                score += 1;
            }
        }
        return score;
    }

    private String normalize(String text) {
        return text == null ? "" : text.toLowerCase(Locale.ROOT);
    }
}

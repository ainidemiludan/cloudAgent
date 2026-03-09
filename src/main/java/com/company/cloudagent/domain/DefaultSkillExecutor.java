package com.company.cloudagent.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultSkillExecutor implements SkillExecutor {

    @Override
    public List<String> execute(List<String> skills, String prompt, List<String> knowledge) {
        return skills.stream().map(skill -> "executed:" + skill).toList();
    }
}

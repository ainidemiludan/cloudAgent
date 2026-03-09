package com.company.cloudagent.domain;

import java.util.List;

public interface SkillExecutor {

    List<String> execute(List<String> skills, String prompt, List<String> knowledge);
}

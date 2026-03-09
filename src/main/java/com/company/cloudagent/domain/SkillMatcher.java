package com.company.cloudagent.domain;

import java.util.List;

public interface SkillMatcher {

    List<String> match(String prompt, List<String> knowledge);
}

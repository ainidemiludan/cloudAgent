package com.company.cloudagent;

import com.company.cloudagent.domain.DefaultSkillExecutor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSkillExecutorTest {

    @Test
    void shouldExecuteSkillsAsMarkers() {
        DefaultSkillExecutor executor = new DefaultSkillExecutor();
        List<String> result = executor.execute(List.of("skill-a", "skill-b"), "prompt", List.of("k1"));
        assertEquals(List.of("executed:skill-a", "executed:skill-b"), result);
    }
}

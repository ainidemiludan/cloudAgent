package com.company.cloudagent;

import com.company.cloudagent.infra.vector.RedisBackedVectorKnowledgeService;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedisBackedVectorKnowledgeServiceTest {

    @Test
    void shouldFallbackWhenRedisConnectionFails() {
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString()))
                .thenThrow(new RedisConnectionFailureException("redis down"));

        RedisBackedVectorKnowledgeService service = new RedisBackedVectorKnowledgeService(redisTemplate);

        List<String> result = service.retrieve("hello");

        assertEquals(List.of("请接入企业向量数据库（Milvus/PGVector）并实现真实召回逻辑"), result);
    }
}

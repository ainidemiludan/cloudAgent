package com.company.cloudagent.infra.vector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisBackedVectorKnowledgeService implements VectorKnowledgeService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public List<String> retrieve(String prompt) {
        String key = "knowledge:" + Integer.toHexString(prompt.hashCode());
        try {
            String cached = redisTemplate.opsForValue().get(key);
            if (cached != null && !cached.isBlank()) {
                return List.of(cached);
            }
        } catch (RedisConnectionFailureException ex) {
            log.warn("Redis unavailable when retrieving knowledge for key={}, fallback to default knowledge", key, ex);
        }
        return List.of("请接入企业向量数据库（Milvus/PGVector）并实现真实召回逻辑");
    }
}

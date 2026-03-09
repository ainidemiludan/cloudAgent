# cloudAgent

企业级云端智能体框架（非个人助手），面向公司内部统一接入与治理。

## 1. 目标与设计理念

参考 openClaw 的核心思想：
- **Agent 云端运行**：能力不落地到个人设备，统一在服务端执行。
- **可组合能力**：通过 Prompt + 向量检索 + Skills 编排满足复杂任务。
- **工程化可运营**：支持审计、可观测、权限治理、异步事件流。

本仓库给出一套基于 **Java 17+ / Spring Boot / Spring AI Alibaba / MySQL / Redis / Kafka** 的企业级落地骨架。

---

## 2. 技术栈（按要求）

- **语言/JDK**：Java 17
- **AI 框架**：Spring AI Alibaba（通过 BOM 管理）
- **关系数据库**：MySQL（会话记录、审计、配置）
- **缓存**：Redis（上下文缓存、召回结果缓存）
- **消息队列**：Kafka（异步事件、日志与指标上报）

---

## 3. 架构分层

```text
[Chat Web/API]
      |
      v
[ChatOrchestrationService]
  |- Prompt 组装
  |- VectorKnowledgeService (RAG)
  |- SkillExecutor (技能编排)
  |- ChatClient (Spring AI Alibaba)
  |- ConversationRepository (MySQL)
  |- AgentEventPublisher (Kafka)
```

### 核心流程
1. 用户在聊天页提交请求（用户ID、会话ID、Prompt、技能集合）。
2. 编排层先进行向量检索（当前示例为 Redis 缓存占位，后续可切换 Milvus/PGVector）。
3. 执行技能链（当前示例为占位执行器，后续可按企业技能注册中心扩展）。
4. 由 Spring AI Alibaba 的 ChatClient 调用模型生成最终回答。
5. 对话落库 MySQL；事件发送 Kafka，供审计/分析/监控消费。

---

## 4. 当前代码能力

- `POST /api/v1/chat`：统一聊天入口。
- `ChatOrchestrationService`：实现“检索 + 技能 + 大模型 + 持久化 + 事件发布”主流程。
- `ConversationRecord`：MySQL 会话记录实体。
- `RedisBackedVectorKnowledgeService`：向量检索接口及缓存占位实现。
- `KafkaAgentEventPublisher`：对话完成事件发布。

---

## 5. 后续企业化增强建议

1. **Skill Marketplace/Registry**
   - 技能元数据、版本、权限域（部门/角色）
   - 灰度发布与回滚策略
2. **RAG 升级**
   - 接入企业知识处理流水线（抽取、切片、Embedding、索引）
   - 引入租户隔离与文档权限过滤
3. **多 Agent 协同**
   - Planner Agent + Tool Agent + Reviewer Agent
   - 关键链路启用人工审批（HITL）
4. **治理与安全**
   - Prompt 注入防护、敏感词过滤、输出合规审查
   - 全链路审计与可观测（Tracing/Token 成本/技能耗时）

---

## 6. 运行准备

1. 启动基础设施：MySQL、Redis、Kafka。
2. 配置 `src/main/resources/application.yml` 中连接信息。
3. 设置环境变量：
   - `DASHSCOPE_API_KEY`
4. 启动应用：

```bash
mvn spring-boot:run
```

调用示例：

```bash
curl -X POST http://localhost:8080/api/v1/chat \
  -H 'Content-Type: application/json' \
  -d '{
    "userId":"u001",
    "sessionId":"s001",
    "prompt":"帮我总结本周研发周报并列出风险",
    "skillNames":["weekly-report-skill","risk-analysis-skill"]
  }'
```

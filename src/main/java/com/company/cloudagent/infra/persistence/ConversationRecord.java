package com.company.cloudagent.infra.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@TableName("conversation_record")
public class ConversationRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String sessionId;

    private String userId;

    private String prompt;

    private String answer;

    private LocalDateTime createdAt = LocalDateTime.now();

    public static ConversationRecord of(String sessionId, String userId, String prompt, String answer) {
        ConversationRecord record = new ConversationRecord();
        record.setSessionId(sessionId);
        record.setUserId(userId);
        record.setPrompt(prompt);
        record.setAnswer(answer);
        return record;
    }
}

package com.company.cloudagent.infra.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_record")
public class ConversationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String sessionId;

    @Column(nullable = false, length = 64)
    private String userId;

    @Lob
    private String prompt;

    @Lob
    private String answer;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public static ConversationRecord of(String sessionId, String userId, String prompt, String answer) {
        ConversationRecord record = new ConversationRecord();
        record.sessionId = sessionId;
        record.userId = userId;
        record.prompt = prompt;
        record.answer = answer;
        return record;
    }
}

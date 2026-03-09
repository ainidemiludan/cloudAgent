package com.company.cloudagent.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<ConversationRecord, Long> {
}

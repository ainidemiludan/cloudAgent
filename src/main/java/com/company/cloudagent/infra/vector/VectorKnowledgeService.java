package com.company.cloudagent.infra.vector;

import java.util.List;

public interface VectorKnowledgeService {

    List<String> retrieve(String prompt);
}

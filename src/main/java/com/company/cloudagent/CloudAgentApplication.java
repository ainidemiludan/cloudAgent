package com.company.cloudagent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CloudAgentApplication {

    public static void main(String[] args) {
        log.info("Starting cloud-agent-platform application...");
        SpringApplication.run(CloudAgentApplication.class, args);
    }
}

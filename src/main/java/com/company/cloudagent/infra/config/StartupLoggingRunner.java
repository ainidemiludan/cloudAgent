package com.company.cloudagent.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLoggingRunner implements ApplicationRunner {

    private final Environment environment;

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.data.redis.host:}")
    private String redisHost;

    @Value("${spring.data.redis.port:}")
    private String redisPort;

    @Value("${spring.kafka.bootstrap-servers:}")
    private String kafkaServers;

    @Value("${spring.ai.alibaba.dashscope.chat.options.model:}")
    private String model;

    @Value("${cloud-agent.mock.enabled:true}")
    private boolean mockEnabled;

    @Override
    public void run(ApplicationArguments args) {
        log.info("CloudAgent started | appName={} | activeProfiles={}",
                environment.getProperty("spring.application.name", "cloud-agent-platform"),
                String.join(",", environment.getActiveProfiles()));
        log.info("Infra endpoints | mysql={} | redis={}:{} | kafka={} | dashscopeModel={} | mockMode={}",
                datasourceUrl, redisHost, redisPort, kafkaServers, model, mockEnabled);
        log.debug("Startup args: {}", args.getOptionNames());
    }
}

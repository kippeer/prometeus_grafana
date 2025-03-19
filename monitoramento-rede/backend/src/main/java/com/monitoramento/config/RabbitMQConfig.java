package com.monitoramento.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String NETWORK_METRICS_QUEUE = "network-metrics";

    @Bean
    public Queue networkMetricsQueue() {
        return new Queue(NETWORK_METRICS_QUEUE, true);
    }
}
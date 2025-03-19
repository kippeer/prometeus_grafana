package com.monitoramento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitoramentoRedeApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitoramentoRedeApplication.class, args);
    }
}
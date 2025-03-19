package com.monitoramento.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class NetworkMetrics {
    private final MeterRegistry registry;

    public NetworkMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordLatency(String destination, double milliseconds) {
        registry.gauge("network.latency", milliseconds);
    }

    public void recordPacketLoss(String destination, double lossPercentage) {
        registry.gauge("network.packet.loss", lossPercentage);
    }

    public void recordBandwidth(String interface_, double mbps) {
        registry.gauge("network.bandwidth", mbps);
    }
}
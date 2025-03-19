package com.monitoramento.service;

import com.monitoramento.metrics.NetworkMetrics;
import com.monitoramento.model.NetworkStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class NetworkMonitoringService {
    private final NetworkMetrics networkMetrics;
    private final RabbitTemplate rabbitTemplate;
    private final ConcurrentMap<String, NetworkStatus> statusHistory;
    private final List<String> defaultTargets;

    public NetworkMonitoringService(NetworkMetrics networkMetrics, RabbitTemplate rabbitTemplate) {
        this.networkMetrics = networkMetrics;
        this.rabbitTemplate = rabbitTemplate;
        this.statusHistory = new ConcurrentHashMap<>();
        this.defaultTargets = new ArrayList<>();
        defaultTargets.add("8.8.8.8");     // Google DNS
        defaultTargets.add("1.1.1.1");     // Cloudflare DNS
        defaultTargets.add("208.67.222.222"); // OpenDNS
    }

    @Scheduled(fixedRate = 60000) // Execute every minute
    public void monitorNetwork() {
        for (String target : defaultTargets) {
            try {
                NetworkStatus status = monitorDestination(target);
                statusHistory.put(target, status);
                publishMetrics(status);
            } catch (Exception e) {
                NetworkStatus errorStatus = new NetworkStatus();
                errorStatus.setDestination(target);
                errorStatus.setReachable(false);
                statusHistory.put(target, errorStatus);
            }
        }
    }

    public NetworkStatus monitorDestination(String destination) throws IOException {
        NetworkStatus status = new NetworkStatus();
        status.setDestination(destination);
        
        InetAddress address = InetAddress.getByName(destination);
        int attempts = 4;
        int successful = 0;
        double totalLatency = 0;
        
        for (int i = 0; i < attempts; i++) {
            long startTime = System.currentTimeMillis();
            boolean reachable = address.isReachable(2000);
            long endTime = System.currentTimeMillis();
            
            if (reachable) {
                successful++;
                totalLatency += (endTime - startTime);
            }
        }
        
        status.setPacketsSent(attempts);
        status.setPacketsLost(attempts - successful);
        status.setReachable(successful > 0);
        
        if (successful > 0) {
            status.setLatency(totalLatency / successful);
        }
        
        return status;
    }

    private void publishMetrics(NetworkStatus status) {
        if (status.isReachable()) {
            networkMetrics.recordLatency(status.getDestination(), status.getLatency());
            double lossPercentage = (status.getPacketsLost() * 100.0) / status.getPacketsSent();
            networkMetrics.recordPacketLoss(status.getDestination(), lossPercentage);
            
            rabbitTemplate.convertAndSend("network-metrics", 
                String.format("Host: %s, Latency: %.2fms, Packet Loss: %.1f%%", 
                    status.getDestination(), 
                    status.getLatency(),
                    lossPercentage));
        }
    }

    public NetworkStatus getLatestStatus(String destination) {
        return statusHistory.get(destination);
    }

    public List<NetworkStatus> getAllCurrentStatus() {
        return new ArrayList<>(statusHistory.values());
    }

    public List<String> getMonitoredTargets() {
        return new ArrayList<>(defaultTargets);
    }
}
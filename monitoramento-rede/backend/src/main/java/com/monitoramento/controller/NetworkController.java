package com.monitoramento.controller;

import com.monitoramento.model.NetworkStatus;
import com.monitoramento.service.NetworkMonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/network")
@CrossOrigin(origins = "*")
public class NetworkController {
    private final NetworkMonitoringService monitoringService;

    public NetworkController(NetworkMonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/status")
    public ResponseEntity<List<NetworkStatus>> getAllStatus() {
        return ResponseEntity.ok(monitoringService.getAllCurrentStatus());
    }

    @GetMapping("/status/{destination}")
    public ResponseEntity<NetworkStatus> getDestinationStatus(@PathVariable String destination) {
        NetworkStatus status = monitoringService.getLatestStatus(destination);
        if (status != null) {
            return ResponseEntity.ok(status);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/targets")
    public ResponseEntity<List<String>> getMonitoredTargets() {
        return ResponseEntity.ok(monitoringService.getMonitoredTargets());
    }

    @PostMapping("/monitor/{destination}")
    public ResponseEntity<NetworkStatus> monitorDestination(@PathVariable String destination) {
        try {
            NetworkStatus status = monitoringService.monitorDestination(destination);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getHealth() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }
}
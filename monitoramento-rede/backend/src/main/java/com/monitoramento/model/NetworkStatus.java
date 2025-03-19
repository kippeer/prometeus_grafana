package com.monitoramento.model;

import java.time.LocalDateTime;

public class NetworkStatus {
    private String destination;
    private double latency;
    private boolean reachable;
    private LocalDateTime timestamp;
    private int packetsSent;
    private int packetsLost;

    public NetworkStatus() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public double getLatency() { return latency; }
    public void setLatency(double latency) { this.latency = latency; }
    
    public boolean isReachable() { return reachable; }
    public void setReachable(boolean reachable) { this.reachable = reachable; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public int getPacketsSent() { return packetsSent; }
    public void setPacketsSent(int packetsSent) { this.packetsSent = packetsSent; }
    
    public int getPacketsLost() { return packetsLost; }
    public void setPacketsLost(int packetsLost) { this.packetsLost = packetsLost; }
}
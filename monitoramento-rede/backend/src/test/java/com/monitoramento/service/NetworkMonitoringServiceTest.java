package com.monitoramento.service;

import com.monitoramento.metrics.NetworkMetrics;
import com.monitoramento.model.NetworkStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;

class NetworkMonitoringServiceTest {
    
    @Mock
    private NetworkMetrics networkMetrics;
    
    @Mock
    private RabbitTemplate rabbitTemplate;
    
    private NetworkMonitoringService service;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new NetworkMonitoringService(networkMetrics, rabbitTemplate);
    }
    
    @Test
    void testMonitorDestination() {
        assertDoesNotThrow(() -> {
            NetworkStatus status = service.monitorDestination("8.8.8.8");
            assertNotNull(status);
            assertEquals("8.8.8.8", status.getDestination());
            assertTrue(status.getPacketsSent() > 0);
        });
    }
    
    @Test
    void testGetMonitoredTargets() {
        var targets = service.getMonitoredTargets();
        assertFalse(targets.isEmpty());
        assertTrue(targets.contains("8.8.8.8"));
        assertTrue(targets.contains("1.1.1.1"));
    }
}
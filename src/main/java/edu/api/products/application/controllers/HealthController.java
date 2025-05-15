package edu.api.products.application.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    private static final String VERSION = "1.0.0";

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthDetails = new HashMap<>();

        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        String uptimeFormatted = formatUptime(uptimeMillis);

        healthDetails.put("status", "UP");
        healthDetails.put("serverTime", getCurrentTime());
        healthDetails.put("uptime", uptimeFormatted);
        healthDetails.put("version", VERSION);

        return ResponseEntity.ok(healthDetails);
    }

    private String getCurrentTime() {
        Instant now = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(now);
    }

    private String formatUptime(long uptimeMillis) {
        long seconds = uptimeMillis / 1000 % 60;
        long minutes = uptimeMillis / (1000 * 60) % 60;
        long hours = uptimeMillis / (1000 * 60 * 60) % 24;
        long days = uptimeMillis / (1000 * 60 * 60 * 24);

        return String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds);
    }
}

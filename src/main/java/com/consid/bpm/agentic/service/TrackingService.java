package com.consid.bpm.agentic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Map;

@Component
public class TrackingService {
    private static final Logger log = LoggerFactory.getLogger(TrackingService.class);

    public Map<String, Object> getTrackingInfo(String orderId) {
        log.info("Looking up tracking infos for order {}", orderId);
        return Map.of(
                "trackingNumber", "J18827747778223882",
                "provider", "DHL",
                "arrivalTime", ZonedDateTime.now().plusDays(2).toInstant().toString()
        );
    }
}

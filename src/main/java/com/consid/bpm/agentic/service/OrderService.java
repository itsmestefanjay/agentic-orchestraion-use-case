package com.consid.bpm.agentic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public static final String UNKNOWN_CUSTOMER = "42";
    private static final String UNKNOWN_ORDER = "42";
    private static final String SHIPPED_ORDER = "43";

    public Optional<String> findOrderById(String orderId) {
        if (orderId == null || orderId.isBlank() || orderId.equalsIgnoreCase(UNKNOWN_ORDER)) {
            log.info("Order not found");
            return Optional.empty();
        } else {
            log.info("Order {} found", orderId);
            return Optional.of(orderId);
        }
    }

    public Map<String, String> getOrdersForCustomer(String customerId) {
        if (customerId == null || customerId.isBlank() || customerId.equals(UNKNOWN_CUSTOMER)) {
            log.info("No orders found for customer {}", customerId);
            return new HashMap<>();
        }
        Map<String, String> result = Map.of(
                LocalDate.now().minusDays(5).toString(), "11223344",
                LocalDate.now().minusDays(4).toString(), "11223556",
                LocalDate.now().minusDays(3).toString(), "11224894",
                LocalDate.now().minusDays(2).toString(), "11231121",
                LocalDate.now().minusDays(1).toString(), "11231314",
                LocalDate.now().toString(), "ORD-11231413"
        );
        log.info("{} orders found for customer {}: {}", result.size(), customerId, result);
        return result;
    }

    public String getOrderStatus(String orderId) {
        if (orderId.isBlank() || orderId.equalsIgnoreCase(UNKNOWN_ORDER)) {
            log.warn("Order status not found");
            return "STATUS_NOT_FOUND";
        }
        if (orderId.equalsIgnoreCase(SHIPPED_ORDER)) {
            log.info("Order status SHIPPED");
            return "SHIPPED";
        }
        log.info("Order status IN_PREPARATION");
        return "IN_PREPARATION";
    }
}

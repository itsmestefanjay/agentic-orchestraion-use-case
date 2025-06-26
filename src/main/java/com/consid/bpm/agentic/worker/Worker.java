package com.consid.bpm.agentic.worker;

import com.consid.bpm.agentic.service.OrderService;
import com.consid.bpm.agentic.service.TrackingService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class Worker {

    private static final Logger log = LoggerFactory.getLogger(Worker.class);

    private final OrderService orderService;
    private final TrackingService trackingService;

    private static final String ORDER_STATUS = "orderStatus";
    private static final String ORDER_DATE = "orderDate";
    private static final String STATUS = "status";
    private static final String ORDER_ID = "orderId";
    private static final String CUSTOMER_ID = "customerId";

    public Worker(OrderService orderService, TrackingService trackingService) {
        this.orderService = orderService;
        this.trackingService = trackingService;
    }

    @JobWorker(type = "search-order", fetchVariables = {"orderInfo"}, streamEnabled = false)
    public Map<String, Object> searchOrder(@Variable Map<String, Object> orderInfo) {
        log.info("Got request {}", orderInfo);
        Optional<String> optionalOrderId = orderService.findOrderById(Objects.requireNonNullElse(orderInfo.get(ORDER_ID), "").toString());

        if (optionalOrderId.isPresent()) {
            return Map.of(STATUS, "Order found", ORDER_ID, optionalOrderId.get());
        }

        Map<String, String> ordersForCustomer = orderService.getOrdersForCustomer(Objects.requireNonNullElse(orderInfo.get(CUSTOMER_ID), "").toString());
        Optional<String> optionalOrderByDate = getOrderByDate(ordersForCustomer, Objects.requireNonNullElse(orderInfo.get(ORDER_DATE), "").toString());

        if (ordersForCustomer.isEmpty() || optionalOrderByDate.isEmpty()) {
            log.info("Customer has {} orders", ordersForCustomer.keySet().size());
            return Map.of(STATUS, "Order not found");
        }

        return Map.of(STATUS, "Order found", ORDER_ID, optionalOrderByDate.get());
    }


    @JobWorker(type = "order-status", fetchVariables = {"orderId"}, streamEnabled = false)
    public Map<String, Object> getOrderStatus(@Variable String orderId) {
        log.info("Searching for order status...");
        String status = orderService.getOrderStatus(orderId);
        return Map.of(ORDER_STATUS, status);
    }

    @JobWorker(type = "get-tracking-code", fetchVariables = {"orderId"}, streamEnabled = false)
    public Map<String, Object> getTrackingCode(@Variable String orderId) {
        log.info("Get tracking code for order {}", orderId);
        return trackingService.getTrackingInfo(orderId);
    }

    @JobWorker(type = "inform-customer", fetchVariables = {"statusMessage"}, streamEnabled = false)
    public Map<String, Object> informCustomer(@Variable Map<String, Object> statusMessage) {
        String message = statusMessage.get("message").toString();
        log.info("Sending message: {}", message);
        return Map.of("message", message);
    }

    private Optional<String> getOrderByDate(Map<String, String> ordersByDate, String orderDate) {
        if (orderDate.isEmpty()) {
            log.info("No order date provided");
            return Optional.empty();
        }
        if (ordersByDate.containsKey(orderDate)) {
            String orderId = ordersByDate.get(orderDate);
            log.info("Order {} found on date {}", orderId, orderDate);
            return Optional.of(ordersByDate.get(orderDate));
        }
        log.info("No order found for date {}", orderDate);
        return Optional.empty();
    }

}

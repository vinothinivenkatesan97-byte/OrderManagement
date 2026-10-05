package com.order.kafka;

import com.order.exception.InvalidOrderException;
import com.order.model.Order;

import com.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class OrderEventConsumer {

    private final OrderService orderService;
    private final RetryTemplate retryTemplate;
    private final KafkaTemplate<String, Object> dlqKafkaTemplate;

    @Autowired
    public OrderEventConsumer(OrderService orderService, RetryTemplate retryTemplate, KafkaTemplate<String, Object> dlqKafkaTemplate) {
        this.orderService = orderService;
        this.retryTemplate = retryTemplate;
        this.dlqKafkaTemplate = dlqKafkaTemplate;
    }

    @Retryable(
    value = {InvalidOrderException.class},
    retryFor = Exception.class,
    backoff = @Backoff(delay = 2000),
    retryTemplate = "retryTemplate"
)
    @KafkaListener(topics = "orders-events", groupId = "order-service-group")
    public void consume(OrderEvent orderEvent) {
        Order order = orderEvent.order();
        if (order.amount() != null && order.amount() < 0) {
            throw new InvalidOrderException("Order amount cannot be negative");
        }
        switch (orderEvent.operation()) {
            case "CREATE", "UPDATE" -> orderService.save(order);
            case "DELETE" -> orderService.deleteOrder(order.orderId());
            default -> throw new InvalidOrderException("Unknown operation "+orderEvent.operation());
        }
    }

    @Recover
    public void recover(OrderEvent orderEvent, Exception exception) {
        dlqKafkaTemplate.send("orders-events-dlq", orderEvent);
    }
}

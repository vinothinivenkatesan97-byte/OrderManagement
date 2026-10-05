package com.order.controller;

import com.order.event.OrderEvent;
import com.order.model.Order;
import com.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderController(OrderService orderService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderService = orderService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody Order order) {
        OrderEvent orderEvent = new OrderEvent("CREATE", order);
        kafkaTemplate.send("order-events", orderEvent);
        orderService.createOrder(order);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        return orderService.getOrder(id).map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable String id, @Valid @RequestBody Order order) {
        OrderEvent orderEvent = new OrderEvent("UPDATE", order);
        kafkaTemplate.send("order-events", orderEvent);
        orderService.updateOrder(id, order);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        Order order = orderService.getOrder(id).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderEvent orderEvent = new OrderEvent("DELETE", order);
        kafkaTemplate.send("order-events", orderEvent);
        orderService.deleteOrder(id);
        return ResponseEntity.accepted().build();
    }
}

package com.order.service;

import com.order.kafka.OrderEvent;
import com.order.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final Map<String, Order> orderMap = new ConcurrentHashMap<>();

    public OrderService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createOrder(Order order) {
        Order pendingOrder = new Order(order.orderId(), order.customerId(), order.amount(),order.vatAmount(), "PENDING");
        OrderEvent orderEvent = new OrderEvent("CREATE", pendingOrder);
        kafkaTemplate.send("orders-events",order.orderId(),orderEvent);
    }

    public Optional<Order> getOrder(String id) {
        return Optional.ofNullable(orderMap.get(id));
    }

    public void updateOrder(String id, Order order) {
        Order updatedOrder = new Order(id, order.customerId(), order.amount(),order.vatAmount(), order.status());
        kafkaTemplate.send("orders-events",id,new OrderEvent("UPDATE",updatedOrder));
    }

    public void deleteOrder(String id) {
        Order order = orderMap.get(id);
        if(order !=null){
            kafkaTemplate.send("orders-events",id,new OrderEvent("DELETE",order));
        }
    }

    public void save(Order order){
        orderMap.put(order.orderId(), order);
    }

    public void delete(String id){
        orderMap.remove(id);
    }
}

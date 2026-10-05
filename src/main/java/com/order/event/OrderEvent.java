package com.order.event;

import com.order.model.Order;

public class OrderEvent {
    private final String eventType;
    private final Order order;

    public OrderEvent(String eventType, Order order) {
        this.eventType = eventType;
        this.order = order;
    }

    public String getEventType() {
        return eventType;
    }

    public Order getOrder() {
        return order;
    }
}
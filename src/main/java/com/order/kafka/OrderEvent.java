package com.order.kafka;

import com.order.model.Order;

public record OrderEvent(
        String operation,
        Order order
) {
}

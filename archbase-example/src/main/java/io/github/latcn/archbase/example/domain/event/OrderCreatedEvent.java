package io.github.latcn.archbase.example.domain.event;

import io.github.latcn.archbase.foundation.event.BaseDomainEvent;

public class OrderCreatedEvent extends BaseDomainEvent {
    private final Long userId;
    private final Long orderId;

    public OrderCreatedEvent(Long orderId, Long userId) {
        super(String.valueOf(orderId));
        this.orderId = orderId;
        this.userId = userId;
    }

    public Long getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
}
package io.github.latcn.archbase.example.domain.model;

import io.github.latcn.archbase.core.exception.BaseException;
import io.github.latcn.archbase.core.exception.ErrorCode;
import io.github.latcn.archbase.foundation.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Order extends BaseEntity<Long> {
    private Long userId;
    private BigDecimal amount;
    private OrderStatus status;
    private List<OrderItem> items;

    public static Order create(Long userId, List<OrderItem> items) {
        Order order = new Order();
        order.userId = userId;
        order.items = items;
        order.amount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.status = OrderStatus.PENDING;
        return order;
    }

    public void pay() {
        if (status != OrderStatus.PENDING) {
            throw BaseException.of(ErrorCode.INVALID_PARAMETER)
                    .set("currentStatus", status)
                    .set("expectedStatus", OrderStatus.PENDING);
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw BaseException.of(ErrorCode.INVALID_PARAMETER)
                    .set("status", status);
        }
        this.status = OrderStatus.CANCELLED;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
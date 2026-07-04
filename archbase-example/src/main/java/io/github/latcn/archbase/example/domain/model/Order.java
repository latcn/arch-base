package io.github.latcn.archbase.example.domain.model;

import io.github.latcn.archbase.core.exception.BaseException;
import io.github.latcn.archbase.core.exception.ErrorCode;
import io.github.latcn.archbase.foundation.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity<Long> {

	private Long userId;

	private BigDecimal amount;

	private OrderStatus status;

	private List<OrderItem> items;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	public static Order create(Long userId, List<OrderItem> items) {
		Order order = new Order();
		order.userId = userId;
		order.items = items;
		order.amount = items.stream().map(OrderItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
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
			throw BaseException.of(ErrorCode.INVALID_PARAMETER).set("status", status);
		}
		this.status = OrderStatus.CANCELLED;
	}

}
package io.github.latcn.archbase.example.domain.model;

import io.github.latcn.archbase.foundation.valueobject.IValueObject;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItem implements IValueObject {

	private Long productId;

	private Integer quantity;

	private BigDecimal price;

	public OrderItem() {
	}

	public OrderItem(Long productId, Integer quantity, BigDecimal price) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}

	public BigDecimal getSubtotal() {
		return price.multiply(BigDecimal.valueOf(quantity));
	}

}
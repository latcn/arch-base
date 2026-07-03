package io.github.latcn.archbase.example.application.command;

import java.math.BigDecimal;

public class OrderItemDTO {

	private Long productId;

	private Integer quantity;

	private BigDecimal price;

	public Long getProductId() {
		return productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

}
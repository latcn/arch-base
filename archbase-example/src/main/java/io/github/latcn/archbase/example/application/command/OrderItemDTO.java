package io.github.latcn.archbase.example.application.command;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemDTO {

	private Long productId;

	private Integer quantity;

	private BigDecimal price;

}
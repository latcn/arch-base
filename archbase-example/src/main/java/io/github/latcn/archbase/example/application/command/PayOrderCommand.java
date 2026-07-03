package io.github.latcn.archbase.example.application.command;

import io.github.latcn.archbase.core.api.ICommand;
import jakarta.validation.constraints.NotNull;

public class PayOrderCommand implements ICommand {

	@NotNull
	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}
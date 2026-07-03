package io.github.latcn.archbase.example.application.response;

import io.github.latcn.archbase.core.api.IResponse;

public class OrderIdResponse implements IResponse {

	private Long orderId;

	public OrderIdResponse() {
	}

	public OrderIdResponse(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}
package io.github.latcn.archbase.example.application.response;

import io.github.latcn.archbase.core.api.IResponse;
import lombok.Data;

@Data
public class OrderIdResponse implements IResponse {

	private Long orderId;

	public OrderIdResponse() {
	}

	public OrderIdResponse(Long orderId) {
		this.orderId = orderId;
	}

}
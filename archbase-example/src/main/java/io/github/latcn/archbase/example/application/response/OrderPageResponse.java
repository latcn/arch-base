package io.github.latcn.archbase.example.application.response;

import io.github.latcn.archbase.core.api.IResponse;
import io.github.latcn.archbase.foundation.query.pagination.PageResult;
import lombok.Data;

@Data
public class OrderPageResponse implements IResponse {

	private PageResult pageResult;

	public OrderPageResponse() {
	}

	public OrderPageResponse(PageResult pageResult) {
		this.pageResult = pageResult;
	}

}
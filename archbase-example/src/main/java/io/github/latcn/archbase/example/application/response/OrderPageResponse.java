package io.github.latcn.archbase.example.application.response;

import io.github.latcn.archbase.core.api.IResponse;
import io.github.latcn.archbase.foundation.pagination.PageResult;
import io.github.latcn.archbase.example.domain.model.Order;

public class OrderPageResponse implements IResponse {
    private PageResult<Order> pageResult;

    public OrderPageResponse() {}

    public OrderPageResponse(PageResult<Order> pageResult) {
        this.pageResult = pageResult;
    }

    public PageResult<Order> getPageResult() { return pageResult; }
    public void setPageResult(PageResult<Order> pageResult) { this.pageResult = pageResult; }
}
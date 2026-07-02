package io.github.latcn.archbase.example.application.query;

import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.example.application.response.OrderPageResponse;

public class OrderPageQuery implements IQuery<OrderPageResponse> {
    private Long userId;
    private int pageNum = 1;
    private int pageSize = 10;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
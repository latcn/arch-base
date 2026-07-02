package io.github.latcn.archbase.example.domain.repository;

import io.github.latcn.archbase.foundation.pagination.PageQuery;
import io.github.latcn.archbase.foundation.pagination.PageResult;
import io.github.latcn.archbase.foundation.repository.IRepository;
import io.github.latcn.archbase.example.domain.model.Order;

public interface IOrderRepository extends IRepository<Order, Long> {
    PageResult<Order> queryByUserId(PageQuery pageQuery, Long userId);
}
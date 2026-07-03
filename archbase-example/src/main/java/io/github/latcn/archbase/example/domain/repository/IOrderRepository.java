package io.github.latcn.archbase.example.domain.repository;

import io.github.latcn.archbase.example.application.query.OrderPageQuery;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.foundation.query.pagination.PageResult;
import io.github.latcn.archbase.foundation.repository.IRepository;

public interface IOrderRepository extends IRepository<Order, Long> {

	PageResult<Order> queryByUserId(OrderPageQuery orderPageQuery);

}
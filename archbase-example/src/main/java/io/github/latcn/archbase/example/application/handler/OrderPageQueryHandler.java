package io.github.latcn.archbase.example.application.handler;

import io.github.latcn.archbase.cqrs.bus.IQueryHandler;
import io.github.latcn.archbase.example.application.query.OrderPageQuery;
import io.github.latcn.archbase.example.application.response.OrderPageResponse;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.repository.IOrderRepository;
import io.github.latcn.archbase.foundation.pagination.PageQuery;
import io.github.latcn.archbase.foundation.pagination.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderPageQueryHandler implements IQueryHandler<OrderPageQuery, OrderPageResponse> {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public OrderPageResponse handle(OrderPageQuery query) {
        PageQuery pageQuery = PageQuery.of(query.getPageNum(), query.getPageSize());
        PageResult<Order> pageResult;
        if (query.getUserId() != null) {
            pageResult = orderRepository.queryByUserId(pageQuery, query.getUserId());
        } else {
            pageResult = orderRepository.pageQuery(pageQuery);
        }
        return new OrderPageResponse(pageResult);
    }
}
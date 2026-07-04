package io.github.latcn.archbase.example.infrastructure.persistence.repository;

import io.github.latcn.archbase.data.mybatis.repository.AbstractBaseRepository;
import io.github.latcn.archbase.example.application.query.OrderPageQuery;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.repository.IOrderRepository;
import io.github.latcn.archbase.example.infrastructure.persistence.entity.OrderDO;
import io.github.latcn.archbase.foundation.query.cond.FilterCondition;
import io.github.latcn.archbase.foundation.query.cond.Operator;
import io.github.latcn.archbase.foundation.query.pagination.PageResult;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends AbstractBaseRepository<Order, OrderDO, Long> implements IOrderRepository {

	@Override
	public PageResult<Order> queryByUserId(OrderPageQuery orderPageQuery) {
		if (orderPageQuery.getUserId() != null) {
			orderPageQuery.setCondition(FilterCondition.of("userId", Operator.EQ, orderPageQuery.getUserId()));
		}
		PageResult<Order> result = dynamicPageQuery(orderPageQuery);
		return result;
	}

}
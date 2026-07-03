package io.github.latcn.archbase.example.infrastructure.persistence.repository;

import io.github.latcn.archbase.data.mybatis.repository.AbstractBaseRepository;
import io.github.latcn.archbase.example.application.query.OrderPageQuery;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.repository.IOrderRepository;
import io.github.latcn.archbase.example.infrastructure.persistence.po.OrderPO;
import io.github.latcn.archbase.foundation.query.cond.FilterCondition;
import io.github.latcn.archbase.foundation.query.cond.Operator;
import io.github.latcn.archbase.foundation.query.pagination.PageResult;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends AbstractBaseRepository<Order, OrderPO, Long> implements IOrderRepository {

	@Override
	public PageResult<Order> queryByUserId(OrderPageQuery orderPageQuery) {
		// 前端传参：sort=userId,asc|createTime,desc
		// 框架自动转换为：user_id ASC, create_time DESC
		if (orderPageQuery.getUserId() != null) {
			orderPageQuery.setCondition(FilterCondition.of("userId", Operator.EQ, orderPageQuery.getUserId()));
		}
		PageResult<Order> result = dynamicPageQuery(orderPageQuery);
		return result;
	}

}
package io.github.latcn.archbase.example.infrastructure.persistence.repository;

import io.github.latcn.archbase.data.mybatis.repository.AbstractBaseRepository;
import io.github.latcn.archbase.foundation.pagination.PageQuery;
import io.github.latcn.archbase.foundation.pagination.PageResult;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.repository.IOrderRepository;
import io.github.latcn.archbase.example.infrastructure.persistence.po.OrderPO;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl 
        extends AbstractBaseRepository<Order, OrderPO, Long> 
        implements IOrderRepository {

    @Override
    public PageResult<Order> queryByUserId(PageQuery pageQuery, Long userId) {
        return pageQuery(pageQuery, wrapper -> 
            wrapper.eq("user_id", userId)
        );
    }
}
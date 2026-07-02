package io.github.latcn.archbase.example.infrastructure.persistence.assembler;

import io.github.latcn.archbase.foundation.assembler.IAssembler;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.model.OrderStatus;
import io.github.latcn.archbase.example.infrastructure.persistence.po.OrderPO;
import org.springframework.stereotype.Component;

@Component
public class OrderAssembler implements IAssembler<Order, OrderPO> {

    @Override
    public OrderPO toPO(Order entity) {
        OrderPO po = new OrderPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setAmount(entity.getAmount());
        po.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return po;
    }

    @Override
    public Order toEntity(OrderPO po) {
        Order order = new Order();
        order.setId(po.getId());
        order.setUserId(po.getUserId());
        order.setAmount(po.getAmount());
        order.setStatus(po.getStatus() != null ? OrderStatus.valueOf(po.getStatus()) : null);
        return order;
    }
}
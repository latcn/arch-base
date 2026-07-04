package io.github.latcn.archbase.example.infrastructure.persistence.assembler;

import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.model.OrderStatus;
import io.github.latcn.archbase.example.infrastructure.persistence.entity.OrderDO;
import io.github.latcn.archbase.foundation.assembler.IAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderAssembler implements IAssembler<Order, OrderDO> {

	@Override
	public OrderDO toDO(Order entity) {
		OrderDO doObj = new OrderDO();
		doObj.setId(entity.getId());
		doObj.setUserId(entity.getUserId());
		doObj.setAmount(entity.getAmount());
		doObj.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
		return doObj;
	}

	@Override
	public Order toEntity(OrderDO doObj) {
		Order order = new Order();
		order.setId(doObj.getId());
		order.setUserId(doObj.getUserId());
		order.setAmount(doObj.getAmount());
		order.setStatus(doObj.getStatus() != null ? OrderStatus.valueOf(doObj.getStatus()) : null);
		return order;
	}

}
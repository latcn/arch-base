package io.github.latcn.archbase.example.application.handler;

import io.github.latcn.archbase.cqrs.bus.ICommandHandler;
import io.github.latcn.archbase.example.application.command.PayOrderCommand;
import io.github.latcn.archbase.example.application.response.OrderIdResponse;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayOrderHandler implements ICommandHandler<PayOrderCommand, OrderIdResponse> {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public OrderIdResponse handle(PayOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId());
        order.pay();
        orderRepository.save(order);
        return new OrderIdResponse(order.getId());
    }
}
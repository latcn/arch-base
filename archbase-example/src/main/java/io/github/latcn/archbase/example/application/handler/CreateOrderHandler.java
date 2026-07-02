package io.github.latcn.archbase.example.application.handler;

import io.github.latcn.archbase.cqrs.bus.ICommandHandler;
import io.github.latcn.archbase.example.application.command.CreateOrderCommand;
import io.github.latcn.archbase.example.application.response.OrderIdResponse;
import io.github.latcn.archbase.example.domain.model.Order;
import io.github.latcn.archbase.example.domain.model.OrderItem;
import io.github.latcn.archbase.example.domain.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateOrderHandler implements ICommandHandler<CreateOrderCommand, OrderIdResponse> {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public OrderIdResponse handle(CreateOrderCommand command) {
        List<OrderItem> items = command.getItems().stream()
                .map(dto -> new OrderItem(dto.getProductId(), dto.getQuantity(), dto.getPrice()))
                .collect(Collectors.toList());

        Order order = Order.create(command.getUserId(), items);
        orderRepository.save(order);

        return new OrderIdResponse(order.getId());
    }
}
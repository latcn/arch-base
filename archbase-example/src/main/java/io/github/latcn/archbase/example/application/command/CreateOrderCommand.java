package io.github.latcn.archbase.example.application.command;

import io.github.latcn.archbase.core.api.ICommand;
import io.github.latcn.archbase.example.application.response.OrderIdResponse;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderCommand implements ICommand<OrderIdResponse> {
    @NotNull
    private Long userId;

    @NotNull
    private List<OrderItemDTO> items;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
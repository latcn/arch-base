package io.github.latcn.archbase.example.application.command;

import io.github.latcn.archbase.core.api.ICommand;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CreateOrderCommand implements ICommand {

	@NotNull
	private Long userId;

	@NotNull
	private List<OrderItemDTO> items;

	public Long getUserId() {
		return userId;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

}
package io.github.latcn.archbase.example.application.command;

import io.github.latcn.archbase.core.api.ICommand;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayOrderCommand implements ICommand {

	@NotNull
	private Long orderId;

}
package io.github.latcn.archbase.example.interfaces.controller;

import io.github.latcn.archbase.cqrs.bus.ICommandBus;
import io.github.latcn.archbase.cqrs.bus.IQueryBus;
import io.github.latcn.archbase.cqrs.controller.BaseController;
import io.github.latcn.archbase.example.application.command.CreateOrderCommand;
import io.github.latcn.archbase.example.application.command.PayOrderCommand;
import io.github.latcn.archbase.example.application.query.OrderPageQuery;
import io.github.latcn.archbase.example.application.response.OrderIdResponse;
import io.github.latcn.archbase.example.application.response.OrderPageResponse;
import io.github.latcn.archbase.foundation.result.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {

	@Autowired
	private ICommandBus commandBus;

	@Autowired
	private IQueryBus queryBus;

	@PostMapping("/create")
	public Result<OrderIdResponse> create(@Valid @RequestBody CreateOrderCommand command) {
		return exec(command);
	}

	@PostMapping("/pay")
	public Result<Void> pay(@Valid @RequestBody PayOrderCommand command) {
		exec(command);
		return Result.success();
	}

	@GetMapping("/page")
	public Result<OrderPageResponse> page(OrderPageQuery query) {
		return exec(query);
	}

}
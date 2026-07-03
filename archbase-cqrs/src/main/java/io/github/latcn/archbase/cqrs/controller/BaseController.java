package io.github.latcn.archbase.cqrs.controller;

import io.github.latcn.archbase.core.api.ICommand;
import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.core.api.IResponse;
import io.github.latcn.archbase.cqrs.bus.ICommandBus;
import io.github.latcn.archbase.cqrs.bus.IQueryBus;
import io.github.latcn.archbase.foundation.result.Result;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller 基类，提供统一的 Command/Query 执行入口 建议使用 Bus 的场景（需满足至少一项）： 1. 系统需要支持消息队列（MQ）做分布式命令分发
 * 2. 系统需要事件溯源（Event Sourcing） 3. 系统需要全量审计拦截（无法用 AOP 精确做到） 其他情况： 直接注入 Handler/Service
 * 调用即可，无需引入 Bus。
 */
public abstract class BaseController {

	@Autowired
	protected ICommandBus commandBus;

	@Autowired
	protected IQueryBus queryBus;

	/**
	 * 执行 Command（返回成功响应）
	 */
	protected <R extends IResponse> Result<R> exec(ICommand command) {
		return Result.success(commandBus.dispatch(command));
	}

	/**
	 * 执行 Query（返回成功响应）
	 */
	protected <R extends IResponse> Result<R> exec(IQuery query) {
		return Result.success(queryBus.dispatch(query));
	}

	/**
	 * 执行 Command（自定义响应处理）
	 */
	protected <R extends IResponse> Result<R> exec(ICommand command, Function<R, Result<R>> responseMapper) {
		R response = commandBus.dispatch(command);
		return responseMapper.apply(response);
	}

}

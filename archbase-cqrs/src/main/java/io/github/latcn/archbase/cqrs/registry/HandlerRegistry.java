package io.github.latcn.archbase.cqrs.registry;

import io.github.latcn.archbase.core.api.ICommand;
import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.cqrs.bus.ICommandBus;
import io.github.latcn.archbase.cqrs.bus.ICommandHandler;
import io.github.latcn.archbase.cqrs.bus.IQueryBus;
import io.github.latcn.archbase.cqrs.bus.IQueryHandler;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

public class HandlerRegistry {

	private final ApplicationContext applicationContext;

	private final ICommandBus commandBus;

	private final IQueryBus queryBus;

	public HandlerRegistry(ApplicationContext applicationContext, ICommandBus commandBus, IQueryBus queryBus) {
		this.applicationContext = applicationContext;
		this.commandBus = commandBus;
		this.queryBus = queryBus;
	}

	public void registerAll() {
		Map<String, ICommandHandler> commandHandlers = applicationContext.getBeansOfType(ICommandHandler.class);
		for (ICommandHandler handler : commandHandlers.values()) {
			Class<? extends ICommand> commandType = resolveCommandType(handler.getClass());
			commandBus.register(commandType, handler);
		}

		Map<String, IQueryHandler> queryHandlers = applicationContext.getBeansOfType(IQueryHandler.class);
		for (IQueryHandler handler : queryHandlers.values()) {
			Class<? extends IQuery> queryType = resolveQueryType(handler.getClass());
			queryBus.register(queryType, handler);
		}
	}

	private Class<? extends ICommand> resolveCommandType(Class<?> handlerClass) {
		Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(handlerClass, ICommandHandler.class);
		return genericTypes != null && genericTypes.length > 0 ? (Class<? extends ICommand>) genericTypes[0] : null;
	}

	private Class<? extends IQuery> resolveQueryType(Class<?> handlerClass) {
		Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(handlerClass, IQueryHandler.class);
		return genericTypes != null && genericTypes.length > 0 ? (Class<? extends IQuery>) genericTypes[0] : null;
	}

}
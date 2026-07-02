package io.github.latcn.archbase.cqrs.registry;

import io.github.latcn.archbase.cqrs.bus.ICommandBus;
import io.github.latcn.archbase.cqrs.bus.ICommandHandler;
import io.github.latcn.archbase.cqrs.bus.IQueryBus;
import io.github.latcn.archbase.cqrs.bus.IQueryHandler;
import io.github.latcn.archbase.cqrs.impl.InMemoryCommandBus;
import io.github.latcn.archbase.cqrs.impl.InMemoryQueryBus;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.util.Map;

public class HandlerRegistry {
    private final ApplicationContext applicationContext;
    private final ICommandBus commandBus;
    private final IQueryBus queryBus;

    public HandlerRegistry(ApplicationContext applicationContext, 
                           ICommandBus commandBus, 
                           IQueryBus queryBus) {
        this.applicationContext = applicationContext;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @SuppressWarnings("rawtypes")
    public void registerAll() {
        Map<String, ICommandHandler> commandHandlers = 
                applicationContext.getBeansOfType(ICommandHandler.class);
        for (ICommandHandler handler : commandHandlers.values()) {
            Class<?> commandType = resolveCommandType(handler.getClass());
            if (commandBus instanceof InMemoryCommandBus && commandType != null) {
                ((InMemoryCommandBus) commandBus).register(commandType, handler);
            }
        }

        Map<String, IQueryHandler> queryHandlers = 
                applicationContext.getBeansOfType(IQueryHandler.class);
        for (IQueryHandler handler : queryHandlers.values()) {
            Class<?> queryType = resolveQueryType(handler.getClass());
            if (queryBus instanceof InMemoryQueryBus && queryType != null) {
                ((InMemoryQueryBus) queryBus).register(queryType, handler);
            }
        }
    }

    private Class<?> resolveCommandType(Class<?> handlerClass) {
        Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(
                handlerClass, ICommandHandler.class);
        return genericTypes != null && genericTypes.length > 0 ? genericTypes[0] : null;
    }

    private Class<?> resolveQueryType(Class<?> handlerClass) {
        Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(
                handlerClass, IQueryHandler.class);
        return genericTypes != null && genericTypes.length > 0 ? genericTypes[0] : null;
    }
}
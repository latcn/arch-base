package io.github.latcn.archbase.cqrs.impl;

import io.github.latcn.archbase.core.api.ICommand;
import io.github.latcn.archbase.core.api.IResponse;
import io.github.latcn.archbase.core.exception.BaseException;
import io.github.latcn.archbase.core.exception.ErrorCode;
import io.github.latcn.archbase.cqrs.bus.ICommandBus;
import io.github.latcn.archbase.cqrs.bus.ICommandHandler;
import io.github.latcn.archbase.cqrs.interceptor.IBusInterceptor;
import io.github.latcn.archbase.cqrs.interceptor.IBusInvocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCommandBus implements ICommandBus {
    private final Map<Class, ICommandHandler> registry = new ConcurrentHashMap<>();
    private final List<IBusInterceptor> interceptors;

    public InMemoryCommandBus() {
        this(null);
    }

    public InMemoryCommandBus(List<IBusInterceptor> interceptors) {
        this.interceptors = interceptors != null ? interceptors : Collections.emptyList();
    }

    public void register(Class commandType, ICommandHandler handler) {
        registry.put(commandType, handler);
    }

    @Override
    public <T, R extends IResponse> R dispatch(ICommand<T> command){
        Class<?> commandType = command.getClass();
        ICommandHandler handler =  registry.get(commandType);

        if (handler == null) {
            throw BaseException.of(ErrorCode.HANDLER_NOT_FOUND)
                    .set("command", commandType.getName());
        }

        CommandInvocation invocation = new CommandInvocation<>(handler, command, interceptors);
        try {
            return (R)invocation.proceed();
        } catch (BaseException e) {
            throw e;
        } catch (Throwable e) {
            throw BaseException.wrap(e, ErrorCode.SYSTEM_ERROR)
                    .set("command", commandType.getName());
        }
    }


    private static class CommandInvocation<C extends ICommand, R extends IResponse> implements IBusInvocation<R> {
        private final ICommandHandler<C,R> handler;
        private final C command;
        private final List<IBusInterceptor> interceptors;
        private int currentIndex = 0;

        CommandInvocation(ICommandHandler<C,R> handler,
                          C command,
                          List<IBusInterceptor> interceptors) {
            this.handler = handler;
            this.command = command;
            this.interceptors = interceptors;
        }

        @Override
        public R proceed() throws Throwable {
            if (currentIndex < interceptors.size()) {
                IBusInterceptor interceptor = interceptors.get(currentIndex++);
                return interceptor.intercept(this);
            } else {
                return handler.handle(command);
            }
        }

        @Override
        public Object getRequest() {
            return command;
        }

        @Override
        public Object getHandler() {
            return handler;
        }
    }
}
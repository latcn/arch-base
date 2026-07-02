package io.github.latcn.archbase.cqrs.bus;

import io.github.latcn.archbase.core.api.ICommand;
import io.github.latcn.archbase.core.api.IResponse;

@FunctionalInterface
public interface ICommandHandler<C extends ICommand, R extends IResponse> {
     R handle(C command);
}
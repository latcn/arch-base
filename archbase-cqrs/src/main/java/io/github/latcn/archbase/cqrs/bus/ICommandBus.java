package io.github.latcn.archbase.cqrs.bus;

import io.github.latcn.archbase.core.api.ICommand;
import io.github.latcn.archbase.core.api.IResponse;

public interface ICommandBus {

    <T, R extends IResponse> R dispatch(ICommand<T> command);
}
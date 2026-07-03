package io.github.latcn.archbase.cqrs.bus;

import io.github.latcn.archbase.core.api.ICommand;
import io.github.latcn.archbase.core.api.IResponse;

public interface ICommandBus {

	void register(Class<? extends ICommand> commandType, ICommandHandler handler);

	<R extends IResponse> R dispatch(ICommand command);

}
package io.github.latcn.archbase.cqrs.bus;

import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.core.api.IResponse;

public interface IQueryBus {

	void register(Class<? extends IQuery> queryType, IQueryHandler handler);

	<R extends IResponse> R dispatch(IQuery query);

}
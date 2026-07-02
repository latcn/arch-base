package io.github.latcn.archbase.cqrs.bus;

import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.core.api.IResponse;

public interface IQueryBus {
    <T, R extends IResponse> R dispatch(IQuery<T> query);
}
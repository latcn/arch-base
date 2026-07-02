package io.github.latcn.archbase.cqrs.bus;

import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.core.api.IResponse;

@FunctionalInterface
public interface IQueryHandler<Q extends IQuery, R extends IResponse> {

    R handle(Q query);
}
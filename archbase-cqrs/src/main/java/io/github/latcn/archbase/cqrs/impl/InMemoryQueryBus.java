package io.github.latcn.archbase.cqrs.impl;

import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.core.api.IResponse;
import io.github.latcn.archbase.core.exception.BaseException;
import io.github.latcn.archbase.core.exception.ErrorCode;
import io.github.latcn.archbase.cqrs.bus.IQueryBus;
import io.github.latcn.archbase.cqrs.bus.IQueryHandler;
import io.github.latcn.archbase.cqrs.interceptor.IBusInterceptor;
import io.github.latcn.archbase.cqrs.interceptor.IBusInvocation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryQueryBus implements IQueryBus {

	private final Map<Class<? extends IQuery>, IQueryHandler> registry = new ConcurrentHashMap<>();

	private final List<IBusInterceptor> interceptors;

	public InMemoryQueryBus() {
		this(null);
	}

	public InMemoryQueryBus(List<IBusInterceptor> interceptors) {
		this.interceptors = interceptors != null ? interceptors : Collections.emptyList();
	}

	@Override
	public void register(Class<? extends IQuery> queryType, IQueryHandler handler) {
		registry.put(queryType, handler);
	}

	@Override
	public <R extends IResponse> R dispatch(IQuery query) {
		Class<?> queryType = query.getClass();
		IQueryHandler handler = registry.get(queryType);
		if (handler == null) {
			throw BaseException.of(ErrorCode.HANDLER_NOT_FOUND).set("query", queryType.getName());
		}

		QueryInvocation invocation = new QueryInvocation<>(handler, query, interceptors);
		try {
			return (R) invocation.proceed();
		}
		catch (BaseException e) {
			throw e;
		}
		catch (Throwable e) {
			throw BaseException.wrap(e, ErrorCode.SYSTEM_ERROR).set("query", queryType.getName());
		}
	}

	private static class QueryInvocation<Q extends IQuery, R extends IResponse> implements IBusInvocation<R> {

		private final IQueryHandler<Q, R> handler;

		private final Q query;

		private final List<IBusInterceptor> interceptors;

		private int currentIndex = 0;

		QueryInvocation(IQueryHandler<Q, R> handler, Q query, List<IBusInterceptor> interceptors) {
			this.handler = handler;
			this.query = query;
			this.interceptors = interceptors;
		}

		@Override
		public R proceed() throws Throwable {
			if (currentIndex < interceptors.size()) {
				IBusInterceptor interceptor = interceptors.get(currentIndex++);
				return interceptor.intercept(this);
			}
			else {
				return handler.handle(query);
			}
		}

		@Override
		public Object getRequest() {
			return query;
		}

		@Override
		public Object getHandler() {
			return handler;
		}

	}

}
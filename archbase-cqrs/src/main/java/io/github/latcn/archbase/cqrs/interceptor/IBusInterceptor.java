package io.github.latcn.archbase.cqrs.interceptor;

@FunctionalInterface
public interface IBusInterceptor {

	<R> R intercept(IBusInvocation invocation) throws Throwable;

}
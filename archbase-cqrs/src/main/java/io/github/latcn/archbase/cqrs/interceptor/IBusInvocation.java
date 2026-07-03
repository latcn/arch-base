package io.github.latcn.archbase.cqrs.interceptor;

public interface IBusInvocation<R> {

	R proceed() throws Throwable;

	Object getRequest();

	Object getHandler();

}
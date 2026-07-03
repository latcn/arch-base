package io.github.latcn.archbase.core.spi;

@FunctionalInterface
public interface CustomExceptionHandler {

	Object handle(Throwable exception);

}
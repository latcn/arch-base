package io.github.latcn.archbase.core.spi;

@FunctionalInterface
public interface ExceptionHandler {
    Object handle(Throwable exception);
}
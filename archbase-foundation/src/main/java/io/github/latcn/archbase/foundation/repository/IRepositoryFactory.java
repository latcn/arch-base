package io.github.latcn.archbase.foundation.repository;

import io.github.latcn.archbase.foundation.assembler.IAssembler;

public interface IRepositoryFactory {

	<T> T getMapper(Class<T> mapperClass);

	<Entity, DO> IAssembler<Entity, DO> getAssembler();

}
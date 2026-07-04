package io.github.latcn.archbase.foundation.assembler;

import java.util.List;
import java.util.stream.Collectors;

public interface IAssembler<Entity, DO> {

	DO toDO(Entity entity);

	Entity toEntity(DO doObj);

	default List<DO> toDOList(List<Entity> entities) {
		return entities.stream().map(this::toDO).collect(Collectors.toList());
	}

	default List<Entity> toEntityList(List<DO> dos) {
		return dos.stream().map(this::toEntity).collect(Collectors.toList());
	}

}
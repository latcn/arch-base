package io.github.latcn.archbase.foundation.assembler;

import java.util.List;
import java.util.stream.Collectors;

public interface IAssembler<Entity, PO> {
    PO toPO(Entity entity);
    Entity toEntity(PO po);

    default List<PO> toPOList(List<Entity> entities) {
        return entities.stream()
                .map(this::toPO)
                .collect(Collectors.toList());
    }

    default List<Entity> toEntityList(List<PO> pos) {
        return pos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
package io.github.latcn.archbase.foundation.repository;

import io.github.latcn.archbase.foundation.entity.BaseEntity;
import io.github.latcn.archbase.foundation.pagination.PageQuery;
import io.github.latcn.archbase.foundation.pagination.PageResult;

import java.util.List;
import java.util.Optional;

public interface IRepository<Entity extends BaseEntity<ID>, ID> {
    void save(Entity entity);
    Entity findById(ID id);
    Optional<Entity> findOptionalById(ID id);
    void delete(Entity entity);
    void deleteById(ID id);
    boolean existsById(ID id);
    List<Entity> findAll();
    PageResult<Entity> pageQuery(PageQuery pageQuery);
}
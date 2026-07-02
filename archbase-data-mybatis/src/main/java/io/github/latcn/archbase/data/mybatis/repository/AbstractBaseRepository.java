package io.github.latcn.archbase.data.mybatis.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.latcn.archbase.core.exception.BaseException;
import io.github.latcn.archbase.core.exception.ErrorCode;
import io.github.latcn.archbase.foundation.assembler.IAssembler;
import io.github.latcn.archbase.foundation.entity.BaseEntity;
import io.github.latcn.archbase.foundation.pagination.PageQuery;
import io.github.latcn.archbase.foundation.pagination.PageResult;
import io.github.latcn.archbase.foundation.repository.IRepository;
import io.github.latcn.archbase.data.mybatis.page.MybatisPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractBaseRepository<Entity extends BaseEntity<ID>, PO, ID extends Serializable>
        implements IRepository<Entity, ID> {

    @Autowired
    protected BaseMapper<PO> baseMapper;

    @Autowired
    protected IAssembler<Entity, PO> assembler;

    @Override
    public void save(Entity entity) {
        PO po = assembler.toPO(entity);
        if (entity.getId() == null) {
            baseMapper.insert(po);
            setIdToEntity(entity, po);
        } else {
            baseMapper.updateById(po);
        }
    }

    @Override
    public Entity findById(ID id) {
        PO po = baseMapper.selectById(id);
        if (po == null) {
            throw BaseException.of(ErrorCode.ENTITY_NOT_FOUND)
                    .set("id", id)
                    .set("entity", getEntityClass().getSimpleName());
        }
        return assembler.toEntity(po);
    }

    @Override
    public Optional<Entity> findOptionalById(ID id) {
        try {
            return Optional.of(findById(id));
        } catch (BaseException e) {
            if (ErrorCode.ENTITY_NOT_FOUND.getCode().equals(e.getCode())) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public void delete(Entity entity) {
        PO po = assembler.toPO(entity);
        baseMapper.deleteById(po);
    }

    @Override
    public void deleteById(ID id) {
        baseMapper.deleteById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return baseMapper.selectCount(
                new QueryWrapper<PO>().eq(getIdFieldName(), id)
        ) > 0;
    }

    @Override
    public List<Entity> findAll() {
        List<PO> pos = baseMapper.selectList(null);
        return assembler.toEntityList(pos);
    }

    @Override
    public PageResult<Entity> pageQuery(PageQuery pageQuery) {
        Page<PO> page = MybatisPageHelper.toPage(pageQuery);
        QueryWrapper<PO> wrapper = buildQueryWrapper(pageQuery);
        Page<PO> result = baseMapper.selectPage(page, wrapper);
        List<Entity> entities = assembler.toEntityList(result.getRecords());
        return PageResult.of(result.getTotal(), entities);
    }

    protected PageResult<Entity> pageQuery(PageQuery pageQuery, 
            Consumer<QueryWrapper<PO>> conditionBuilder) {
        Page<PO> page = MybatisPageHelper.toPage(pageQuery);
        QueryWrapper<PO> wrapper = new QueryWrapper<>();
        conditionBuilder.accept(wrapper);
        MybatisPageHelper.applySort(pageQuery, wrapper);
        Page<PO> result = baseMapper.selectPage(page, wrapper);
        List<Entity> entities = assembler.toEntityList(result.getRecords());
        return PageResult.of(result.getTotal(), entities);
    }

    protected QueryWrapper<PO> buildQueryWrapper(PageQuery pageQuery) {
        QueryWrapper<PO> wrapper = new QueryWrapper<>();
        MybatisPageHelper.applySort(pageQuery, wrapper);
        return wrapper;
    }

    protected void setIdToEntity(Entity entity, PO po) {
        try {
            Method setIdMethod = entity.getClass().getMethod("setId", Object.class);
            Method getIdMethod = po.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(po);
            setIdMethod.invoke(entity, id);
        } catch (Exception e) {
        }
    }

    protected Class<Entity> getEntityClass() {
        return (Class<Entity>) GenericTypeResolver.resolveTypeArguments(
                getClass(), AbstractBaseRepository.class)[0];
    }

    protected String getIdFieldName() {
        return "id";
    }
}
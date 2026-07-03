package io.github.latcn.archbase.data.mybatis.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.latcn.archbase.core.exception.BaseException;
import io.github.latcn.archbase.core.exception.ErrorCode;
import io.github.latcn.archbase.data.mybatis.cond.ConditionParser;
import io.github.latcn.archbase.foundation.assembler.IAssembler;
import io.github.latcn.archbase.foundation.entity.BaseEntity;
import io.github.latcn.archbase.foundation.query.pagination.PageQuery;
import io.github.latcn.archbase.foundation.query.pagination.PageResult;
import io.github.latcn.archbase.foundation.query.pagination.SortDirection;
import io.github.latcn.archbase.foundation.query.pagination.SortItem;
import io.github.latcn.archbase.foundation.repository.IRepository;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

/**
 * 基础仓储实现抽象类 封装 Entity <-> PO 转换及通用 CRUD
 *
 * @param <Entity> 领域实体类型（继承 BaseEntity）
 * @param <PO> 持久化对象类型（MyBatis-Plus 映射类）
 * @param <ID> 标识类型
 */
public abstract class AbstractBaseRepository<Entity extends BaseEntity<ID>, PO, ID extends Serializable>
		implements IRepository<Entity, ID> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

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
		}
		else {
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
		}
		catch (BaseException e) {
			if (ErrorCode.ENTITY_NOT_FOUND.getCode().equals(e.getCode())) {
				return Optional.empty();
			}
			throw e;
		}
	}

	@Override
	public void delete(Entity entity) {
		PO po = assembler.toPO(entity);
		baseMapper.deleteById(getIdFromPO(po));
	}

	@Override
	public void deleteById(ID id) {
		baseMapper.deleteById(id);
	}

	@Override
	public boolean existsById(ID id) {
		QueryWrapper<PO> wrapper = new QueryWrapper<>();
		wrapper.eq(getIdFieldName(), id);
		return baseMapper.selectCount(wrapper) > 0;
	}

	@Override
	public List<Entity> findAll() {
		List<PO> pos = baseMapper.selectList(null);
		return assembler.toEntityList(pos);
	}

	/**
	 * 动态查询（单表）- 使用 PageQuery
	 * @param pageQuery 查询请求（含分页、排序、条件树）
	 * @return 分页结果
	 */
	@Override
	public PageResult<Entity> dynamicPageQuery(PageQuery pageQuery) {
		QueryWrapper<PO> wrapper = new QueryWrapper<>();

		// 1. 应用条件树
		ConditionParser.applyCondition(wrapper, pageQuery.getCondition(), getPOClass());

		// 2. 应用排序
		if (pageQuery.getSorts() != null && !pageQuery.getSorts().isEmpty()) {
			for (SortItem sort : pageQuery.getSorts()) {
				String field = sort.getField();
				if (field == null || field.isEmpty())
					continue;
				// 字段校验：通过 TableInfo 检查是否存在
				Map<String, String> columnMap = ConditionParser.getColumnMap(getPOClass());
				String column = columnMap.get(field);
				if (column == null) {
					log.debug("忽略未知排序字段: {}", field);
					continue;
				}
				if (sort.getDirection() == SortDirection.DESC) {
					wrapper.orderByDesc(column);
				}
				else {
					wrapper.orderByAsc(column);
				}
			}
		}

		// 3. 执行分页
		Page<PO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		page.setSearchCount(pageQuery.isSearchCount());
		Page<PO> result = baseMapper.selectPage(page, wrapper);
		List<Entity> entities = assembler.toEntityList(result.getRecords());
		return PageResult.of(result.getTotal(), entities);
	}

	/**
	 * 动态查询（非分页）- 使用 PageQuery
	 */
	protected List<Entity> dynamicList(PageQuery pageQuery) {
		QueryWrapper<PO> wrapper = new QueryWrapper<>();
		ConditionParser.applyCondition(wrapper, pageQuery.getCondition(), getPOClass());
		// 排序
		applySorts(wrapper, pageQuery.getSorts());
		List<PO> pos = baseMapper.selectList(wrapper);
		return assembler.toEntityList(pos);
	}

	/**
	 * 辅助方法：应用排序到 QueryWrapper
	 * @param wrapper
	 * @param sorts
	 */
	private void applySorts(QueryWrapper<PO> wrapper, List<SortItem> sorts) {
		if (sorts == null)
			return;
		Map<String, String> columnMap = ConditionParser.getColumnMap(getPOClass());
		for (SortItem sort : sorts) {
			String column = columnMap.get(sort.getField());
			if (column == null)
				continue;
			if (sort.getDirection() == SortDirection.DESC) {
				wrapper.orderByDesc(column);
			}
			else {
				wrapper.orderByAsc(column);
			}
		}
	}

	/**
	 * 设置 ID 到实体（用于新增后回写 ID）
	 */
	protected void setIdToEntity(Entity entity, PO po) {
		try {
			Method poGetIdMethod = po.getClass().getMethod("getId");
			Method entitySetIdMethod = entity.getClass().getMethod("setId", Object.class);
			Object id = poGetIdMethod.invoke(po);
			entitySetIdMethod.invoke(entity, id);
		}
		catch (Exception e) {
			log.debug("回写 ID 失败，子类可重写此方法实现: {}", e.getMessage());
		}
	}

	/**
	 * 从 PO 中获取 ID
	 */
	protected ID getIdFromPO(PO po) {
		try {
			Method getter = po.getClass().getMethod("getId");
			return (ID) getter.invoke(po);
		}
		catch (Exception e) {
			log.warn("获取 PO ID 失败", e);
			return null;
		}
	}

	/**
	 * 获取实体类类型（用于错误信息和泛型处理）
	 */
	protected Class<Entity> getEntityClass() {
		Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractBaseRepository.class);
		return genericTypes != null && genericTypes.length > 0 ? (Class<Entity>) genericTypes[0] : null;
	}

	/**
	 * 获取 PO 类类型（用于 TableInfoHelper）
	 */
	protected Class<PO> getPOClass() {
		Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractBaseRepository.class);
		return genericTypes != null && genericTypes.length > 0 ? (Class<PO>) genericTypes[1] : null;
	}

	/**
	 * 获取 ID 字段名（用于 existsById）
	 */
	protected String getIdFieldName() {
		return "id";
	}

}
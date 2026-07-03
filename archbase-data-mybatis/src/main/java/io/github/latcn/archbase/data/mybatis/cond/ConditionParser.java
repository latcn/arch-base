package io.github.latcn.archbase.data.mybatis.cond;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import io.github.latcn.archbase.foundation.query.cond.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 条件解析器：将条件树转换为 MyBatis-Plus QueryWrapper
 * <p>
 * 安全特性：字段名必须存在于实体类中，否则忽略（不抛异常）
 */
public final class ConditionParser {

	private static final Logger log = LoggerFactory.getLogger(ConditionParser.class);

	/**
	 * 缓存实体类的列名映射（属性名 → 列名）
	 */
	private static final Map<Class<?>, Map<String, String>> COLUMN_MAP_CACHE = new ConcurrentHashMap<>();

	private ConditionParser() {
	}

	/**
	 * 应用条件树到 QueryWrapper
	 * @param wrapper QueryWrapper
	 * @param condition 条件树根节点
	 * @param entityClass 实体类类型（用于获取字段映射）
	 * @param <T> 实体类型
	 */
	public static <T> void applyCondition(QueryWrapper<T> wrapper, Condition condition, Class<T> entityClass) {
		if (condition == null) {
			return;
		}
		Map<String, String> columnMap = getColumnMap(entityClass);
		if (columnMap.isEmpty()) {
			log.warn("实体类 {} 未加载 TableInfo，无法解析条件", entityClass.getName());
			return;
		}
		doApply(wrapper, condition, columnMap);
	}

	/**
	 * 递归应用条件
	 */
	private static <T> void doApply(QueryWrapper<T> wrapper, Condition condition, Map<String, String> columnMap) {
		if (condition == null) {
			return;
		}

		if (condition instanceof LogicGroup) {
			LogicGroup group = (LogicGroup) condition;
			if (group.getConditions() == null || group.getConditions().isEmpty()) {
				return;
			}

			boolean isAnd = group.getLogic() == Logic.AND;
			// 使用 wrapper.and() 或 wrapper.or() 包裹子条件
			if (isAnd) {
				wrapper.and(w -> {
					for (Condition child : group.getConditions()) {
						applyChild(w, child, columnMap);
					}
				});
			}
			else {
				wrapper.or(w -> {
					for (Condition child : group.getConditions()) {
						applyChild(w, child, columnMap);
					}
				});
			}
		}
		else if (condition instanceof FilterCondition) {
			applyFilter(wrapper, (FilterCondition) condition, columnMap);
		}
		else {
			log.warn("未知条件类型: {}", condition.getClass().getName());
		}
	}

	/**
	 * 处理子条件（用于 and/or 回调内部）
	 */
	private static <T> void applyChild(QueryWrapper<T> wrapper, Condition condition, Map<String, String> columnMap) {
		if (condition instanceof LogicGroup) {
			// 递归处理嵌套逻辑组
			LogicGroup group = (LogicGroup) condition;
			boolean isAnd = group.getLogic() == Logic.AND;
			if (isAnd) {
				wrapper.and(w -> {
					for (Condition child : group.getConditions()) {
						applyChild(w, child, columnMap);
					}
				});
			}
			else {
				wrapper.or(w -> {
					for (Condition child : group.getConditions()) {
						applyChild(w, child, columnMap);
					}
				});
			}
		}
		else if (condition instanceof FilterCondition) {
			applyFilter(wrapper, (FilterCondition) condition, columnMap);
		}
	}

	/**
	 * 应用单个过滤条件
	 */
	private static <T> void applyFilter(QueryWrapper<T> wrapper, FilterCondition filter,
			Map<String, String> columnMap) {
		String field = filter.getField();
		if (field == null || field.isEmpty()) {
			return;
		}

		// 字段名到列名的映射（忽略不存在的字段）
		String column = columnMap.get(field);
		if (column == null) {
			log.debug("忽略未知字段: {}", field);
			return;
		}

		Operator op = filter.getOperator();
		Object value = filter.getValue();
		Object value2 = filter.getValue2();

		switch (op) {
			case EQ:
				if (value != null)
					wrapper.eq(column, value);
				break;
			case NE:
				if (value != null)
					wrapper.ne(column, value);
				break;
			case GT:
				if (value != null)
					wrapper.gt(column, value);
				break;
			case GE:
				if (value != null)
					wrapper.ge(column, value);
				break;
			case LT:
				if (value != null)
					wrapper.lt(column, value);
				break;
			case LE:
				if (value != null)
					wrapper.le(column, value);
				break;
			case LIKE:
				if (value != null)
					wrapper.like(column, value.toString());
				break;
			case LIKE_LEFT:
				if (value != null)
					wrapper.likeLeft(column, value.toString());
				break;
			case LIKE_RIGHT:
				if (value != null)
					wrapper.likeRight(column, value.toString());
				break;
			case IN:
				if (value instanceof Collection) {
					wrapper.in(column, (Collection) value);
				}
				else if (value != null) {
					wrapper.in(column, value);
				}
				break;
			case NOT_IN:
				if (value instanceof Collection) {
					wrapper.notIn(column, (Collection) value);
				}
				else if (value != null) {
					wrapper.notIn(column, value);
				}
				break;
			case BETWEEN:
				if (value != null && value2 != null) {
					wrapper.between(column, value, value2);
				}
				break;
			case IS_NULL:
				wrapper.isNull(column);
				break;
			case IS_NOT_NULL:
				wrapper.isNotNull(column);
				break;
			default:
				log.warn("未处理的操作符: {}", op);
		}
	}

	/**
	 * 获取实体类的列名映射（带缓存）
	 */
	public static Map<String, String> getColumnMap(Class<?> entityClass) {
		return COLUMN_MAP_CACHE.computeIfAbsent(entityClass, clazz -> {
			TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
			if (tableInfo == null) {
				log.warn("无法获取实体类 {} 的 TableInfo，字段映射为空", clazz.getName());
				return new ConcurrentHashMap<>();
			}
			Map<String, String> map = new ConcurrentHashMap<>();
			tableInfo.getFieldList().forEach(fieldInfo -> {
				map.put(fieldInfo.getProperty(), fieldInfo.getColumn());
			});
			// 主键
			if (tableInfo.getKeyProperty() != null && tableInfo.getKeyColumn() != null) {
				map.put(tableInfo.getKeyProperty(), tableInfo.getKeyColumn());
			}
			log.debug("加载实体类 {} 的字段映射，共 {} 个字段", clazz.getSimpleName(), map.size());
			return map;
		});
	}

}

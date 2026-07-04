package io.github.latcn.archbase.data.mybatis.cond;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.latcn.archbase.foundation.query.cond.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConditionParser {

	private static final Logger log = LoggerFactory.getLogger(ConditionParser.class);

	private static final Map<Class<?>, Map<String, String>> COLUMN_MAP_CACHE = new ConcurrentHashMap<>();

	private ConditionParser() {
	}

	public static <T> void applyCondition(QueryWrapper<T> wrapper, Condition condition, Class<T> entityClass) {
		if (condition == null) {
			return;
		}
		Map<String, String> columnMap = getColumnMap(entityClass);
		if (columnMap.isEmpty()) {
			log.warn("实体类 {} 未加载字段映射，无法解析条件", entityClass.getName());
			return;
		}
		if (condition instanceof LogicGroup) {
			applyLogicGroup(wrapper, (LogicGroup) condition, columnMap);
		}
		else if (condition instanceof FilterCondition) {
			applyFilter(wrapper, (FilterCondition) condition, columnMap);
		}
	}

	private static <T> void applyLogicGroup(QueryWrapper<T> wrapper, LogicGroup group, Map<String, String> columnMap) {
		if (group.getConditions() == null || group.getConditions().isEmpty()) {
			return;
		}

		boolean isAnd = group.getLogic() == Logic.AND;
		boolean first = true;
		for (Condition child : group.getConditions()) {
			if (!first && !isAnd) {
				wrapper.or();
			}
			if (child instanceof LogicGroup) {
				wrapper.nested(w -> applyLogicGroup(w, (LogicGroup) child, columnMap));
			}
			else if (child instanceof FilterCondition) {
				applyFilter(wrapper, (FilterCondition) child, columnMap);
			}
			first = false;
		}
	}

	private static <T> void applyFilter(QueryWrapper<T> wrapper, FilterCondition filter,
			Map<String, String> columnMap) {
		String field = filter.getField();
		if (field == null || field.isEmpty()) {
			return;
		}

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
				if (value != null && value2 != null)
					wrapper.between(column, value, value2);
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

	public static Map<String, String> getColumnMap(Class<?> entityClass) {
		return COLUMN_MAP_CACHE.computeIfAbsent(entityClass, clazz -> {
			Map<String, String> columnMap = new ConcurrentHashMap<>();
			for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				String propertyName = field.getName();
				String columnName = resolveColumnName(field);
				columnMap.put(propertyName, columnName);
			}
			log.debug("加载实体类 {} 的字段映射，共 {} 个字段", clazz.getSimpleName(), columnMap.size());
			return columnMap;
		});
	}

	private static String resolveColumnName(java.lang.reflect.Field field) {
		com.baomidou.mybatisplus.annotation.TableField tableField = field
			.getAnnotation(com.baomidou.mybatisplus.annotation.TableField.class);
		if (tableField != null && !tableField.value().isEmpty()) {
			return tableField.value();
		}
		com.baomidou.mybatisplus.annotation.TableId tableId = field
			.getAnnotation(com.baomidou.mybatisplus.annotation.TableId.class);
		if (tableId != null && !tableId.value().isEmpty()) {
			return tableId.value();
		}
		return toUnderlineCase(field.getName());
	}

	private static String toUnderlineCase(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0) {
					sb.append('_');
				}
				sb.append(Character.toLowerCase(c));
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
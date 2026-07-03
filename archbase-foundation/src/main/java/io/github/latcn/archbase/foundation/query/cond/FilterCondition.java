package io.github.latcn.archbase.foundation.query.cond;

import lombok.Data;

/**
 * 叶子条件：具体字段的过滤条件
 */
@Data
public class FilterCondition implements Condition {

	/**
	 * 字段名（实体属性名）
	 */
	private String field;

	/**
	 * 操作符
	 */
	private Operator operator;

	/**
	 * 值（单值）
	 */
	private Object value;

	/**
	 * 第二个值（BETWEEN 时使用）
	 */
	private Object value2;

	// 静态工厂方法便于构建
	public static FilterCondition of(String field, Operator operator, Object value) {
		FilterCondition cond = new FilterCondition();
		cond.setField(field);
		cond.setOperator(operator);
		cond.setValue(value);
		return cond;
	}

	public static FilterCondition between(String field, Object start, Object end) {
		FilterCondition cond = new FilterCondition();
		cond.setField(field);
		cond.setOperator(Operator.BETWEEN);
		cond.setValue(start);
		cond.setValue2(end);
		return cond;
	}

}

package io.github.latcn.archbase.foundation.query.cond;

/**
 * 查询操作符
 */
public enum Operator {

	EQ, // 等于 =
	NE, // 不等于 <>
	GT, // 大于 >
	GE, // 大于等于 >=
	LT, // 小于 <
	LE, // 小于等于 <=
	LIKE, // 模糊匹配 %value%
	LIKE_LEFT, // 左匹配 value%
	LIKE_RIGHT, // 右匹配 %value
	IN, // IN (value1, value2, ...)
	NOT_IN, // NOT IN (...)
	BETWEEN, // BETWEEN value1 AND value2
	IS_NULL, // IS NULL
	IS_NOT_NULL // IS NOT NULL

}

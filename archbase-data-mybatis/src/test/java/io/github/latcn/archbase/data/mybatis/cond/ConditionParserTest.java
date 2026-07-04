package io.github.latcn.archbase.data.mybatis.cond;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import io.github.latcn.archbase.foundation.query.cond.FilterCondition;
import io.github.latcn.archbase.foundation.query.cond.LogicGroup;
import io.github.latcn.archbase.foundation.query.cond.Operator;
import java.util.List;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConditionParserTest {

	private QueryWrapper<TestEntity> wrapper;

	@BeforeAll
	static void initTableInfo() {
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new Configuration(), ""), TestEntity.class);
	}

	@BeforeEach
	void setUp() {
		wrapper = new QueryWrapper<>();
	}

	private String getSqlSegment(QueryWrapper<?> wrapper) {
		String sql = wrapper.getSqlSegment();
		System.out.println("Generated SQL: " + sql);
		return sql;
	}

	@Test
	@DisplayName("测试 EQ 操作符")
	void testEqOperator() {
		FilterCondition condition = FilterCondition.of("userId", Operator.EQ, 1001);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(user_id = #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 NE 操作符")
	void testNeOperator() {
		FilterCondition condition = FilterCondition.of("status", Operator.NE, "CANCELLED");
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status <> #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 GT 操作符")
	void testGtOperator() {
		FilterCondition condition = FilterCondition.of("amount", Operator.GT, 100);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(amount > #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 GE 操作符")
	void testGeOperator() {
		FilterCondition condition = FilterCondition.of("amount", Operator.GE, 100);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(amount >= #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 LT 操作符")
	void testLtOperator() {
		FilterCondition condition = FilterCondition.of("amount", Operator.LT, 200);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(amount < #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 LE 操作符")
	void testLeOperator() {
		FilterCondition condition = FilterCondition.of("amount", Operator.LE, 200);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(amount <= #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 LIKE 操作符")
	void testLikeOperator() {
		FilterCondition condition = FilterCondition.of("status", Operator.LIKE, "PENDING");
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status LIKE #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 LIKE_LEFT 操作符")
	void testLikeLeftOperator() {
		FilterCondition condition = FilterCondition.of("status", Operator.LIKE_LEFT, "ING");
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status LIKE #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 LIKE_RIGHT 操作符")
	void testLikeRightOperator() {
		FilterCondition condition = FilterCondition.of("status", Operator.LIKE_RIGHT, "PEND");
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status LIKE #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试 IN 操作符（集合）")
	void testInOperatorWithCollection() {
		FilterCondition condition = FilterCondition.of("status", Operator.IN, List.of("PENDING", "PAID"));
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status IN (#{ew.paramNameValuePairs.MPGENVAL1},#{ew.paramNameValuePairs.MPGENVAL2}))", sql);
	}

	@Test
	@DisplayName("测试 IN 操作符（单值）")
	void testInOperatorWithSingleValue() {
		FilterCondition condition = FilterCondition.of("userId", Operator.IN, 1001);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(user_id IN (#{ew.paramNameValuePairs.MPGENVAL1}))", sql);
	}

	@Test
	@DisplayName("测试 NOT_IN 操作符")
	void testNotInOperator() {
		FilterCondition condition = FilterCondition.of("status", Operator.NOT_IN, List.of("CANCELLED", "DELETED"));
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status NOT IN (#{ew.paramNameValuePairs.MPGENVAL1},#{ew.paramNameValuePairs.MPGENVAL2}))", sql);
	}

	@Test
	@DisplayName("测试 BETWEEN 操作符")
	void testBetweenOperator() {
		FilterCondition condition = FilterCondition.between("amount", 100, 200);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(amount BETWEEN #{ew.paramNameValuePairs.MPGENVAL1} AND #{ew.paramNameValuePairs.MPGENVAL2})",
				sql);
	}

	@Test
	@DisplayName("测试 IS_NULL 操作符")
	void testIsNullOperator() {
		FilterCondition condition = FilterCondition.of("status", Operator.IS_NULL, null);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status IS NULL)", sql);
	}

	@Test
	@DisplayName("测试 IS_NOT_NULL 操作符")
	void testIsNotNullOperator() {
		FilterCondition condition = FilterCondition.of("status", Operator.IS_NOT_NULL, null);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status IS NOT NULL)", sql);
	}

	@Test
	@DisplayName("测试 AND 逻辑组")
	void testAndLogicGroup() {
		LogicGroup group = LogicGroup.and(FilterCondition.of("userId", Operator.EQ, 1001),
				FilterCondition.of("status", Operator.EQ, "PAID"));
		ConditionParser.applyCondition(wrapper, group, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(user_id = #{ew.paramNameValuePairs.MPGENVAL1} AND status = #{ew.paramNameValuePairs.MPGENVAL2})",
				sql);
	}

	@Test
	@DisplayName("测试 OR 逻辑组")
	void testOrLogicGroup() {
		LogicGroup group = LogicGroup.or(FilterCondition.of("status", Operator.EQ, "PENDING"),
				FilterCondition.of("status", Operator.EQ, "PAID"));
		ConditionParser.applyCondition(wrapper, group, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(status = #{ew.paramNameValuePairs.MPGENVAL1} OR status = #{ew.paramNameValuePairs.MPGENVAL2})",
				sql);
	}

	@Test
	@DisplayName("测试嵌套逻辑组")
	void testNestedLogicGroup() {
		LogicGroup group = LogicGroup.and(
				LogicGroup.or(FilterCondition.of("userId", Operator.EQ, 1001),
						FilterCondition.of("userId", Operator.EQ, 1002)),
				FilterCondition.of("amount", Operator.GT, 100));
		ConditionParser.applyCondition(wrapper, group, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals(
				"((user_id = #{ew.paramNameValuePairs.MPGENVAL1} OR user_id = #{ew.paramNameValuePairs.MPGENVAL2}) AND amount > #{ew.paramNameValuePairs.MPGENVAL3})",
				sql);
	}

	@Test
	@DisplayName("测试多层嵌套逻辑组")
	void testDeepNestedLogicGroup() {
		LogicGroup group = LogicGroup.and(
				LogicGroup.or(FilterCondition.of("userId", Operator.EQ, 1001),
						LogicGroup.and(FilterCondition.of("status", Operator.EQ, "PAID"),
								FilterCondition.of("amount", Operator.GT, 200))),
				FilterCondition.of("isDeleted", Operator.EQ, 0));
		ConditionParser.applyCondition(wrapper, group, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals(
				"((user_id = #{ew.paramNameValuePairs.MPGENVAL1} OR (status = #{ew.paramNameValuePairs.MPGENVAL2} AND amount > #{ew.paramNameValuePairs.MPGENVAL3})) AND is_deleted = #{ew.paramNameValuePairs.MPGENVAL4})",
				sql);
	}

	@Test
	@DisplayName("测试空条件")
	void testNullCondition() {
		ConditionParser.applyCondition(wrapper, null, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("", sql);
	}

	@Test
	@DisplayName("测试空逻辑组")
	void testEmptyLogicGroup() {
		LogicGroup group = LogicGroup.and();
		ConditionParser.applyCondition(wrapper, group, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("", sql);
	}

	@Test
	@DisplayName("测试未知字段被忽略")
	void testUnknownFieldIgnored() {
		FilterCondition condition = FilterCondition.of("unknownField", Operator.EQ, "value");
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("", sql);
	}

	@Test
	@DisplayName("测试字段名到列名的映射")
	void testFieldToColumnMapping() {
		FilterCondition condition = FilterCondition.of("isDeleted", Operator.EQ, 0);
		ConditionParser.applyCondition(wrapper, condition, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals("(is_deleted = #{ew.paramNameValuePairs.MPGENVAL1})", sql);
	}

	@Test
	@DisplayName("测试多个条件组合")
	void testMultipleConditions() {
		LogicGroup group = LogicGroup.and(FilterCondition.of("userId", Operator.EQ, 1001),
				FilterCondition.of("status", Operator.IN, List.of("PENDING", "PAID")),
				FilterCondition.between("amount", 100, 500),
				FilterCondition.of("createTime", Operator.IS_NOT_NULL, null));
		ConditionParser.applyCondition(wrapper, group, TestEntity.class);
		String sql = getSqlSegment(wrapper);
		assertEquals(
				"(user_id = #{ew.paramNameValuePairs.MPGENVAL1} AND status IN (#{ew.paramNameValuePairs.MPGENVAL2},#{ew.paramNameValuePairs.MPGENVAL3}) AND amount BETWEEN #{ew.paramNameValuePairs.MPGENVAL4} AND #{ew.paramNameValuePairs.MPGENVAL5} AND create_time IS NOT NULL)",
				sql);
	}

}

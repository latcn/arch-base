package io.github.latcn.archbase.foundation.query.cond;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 * 逻辑组：由 AND/OR 连接多个子条件
 */
@Data
public class LogicGroup implements Condition {

	/**
	 * 逻辑类型：AND / OR
	 */
	private Logic logic;

	/**
	 * 子条件列表
	 */
	private List<Condition> conditions;

	public LogicGroup(Logic logic, List<Condition> conditions) {
		this.logic = logic;
		this.conditions = conditions != null ? conditions : new ArrayList<>();
	}

	public LogicGroup(Logic logic, Condition... conditions) {
		this(logic, Arrays.asList(conditions));
	}

	public static LogicGroup and(Condition... conditions) {
		return new LogicGroup(Logic.AND, conditions);
	}

	public static LogicGroup or(Condition... conditions) {
		return new LogicGroup(Logic.OR, conditions);
	}

	public void addCondition(Condition condition) {
		if (condition != null) {
			this.conditions.add(condition);
		}
	}

}

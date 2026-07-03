package io.github.latcn.archbase.foundation.query.pagination;

import java.util.Objects;

/**
 * 排序项
 *
 * <p>
 * 表示一个字段的排序规则（字段名 + 排序方向）
 */
public class SortItem {

	/** 排序字段名 */
	private String field;

	/** 排序方向 */
	private SortDirection direction;

	// ==================== 构造方法 ====================

	public SortItem() {
	}

	public SortItem(String field, SortDirection direction) {
		this.field = field;
		this.direction = direction;
	}

	// ==================== 静态工厂方法 ====================

	/**
	 * 创建升序排序项
	 */
	public static SortItem asc(String field) {
		return new SortItem(field, SortDirection.ASC);
	}

	/**
	 * 创建降序排序项
	 */
	public static SortItem desc(String field) {
		return new SortItem(field, SortDirection.DESC);
	}

	// ==================== Getter/Setter ====================

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public SortDirection getDirection() {
		return direction;
	}

	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}

	/**
	 * 判断是否为升序
	 */
	public boolean isAsc() {
		return direction == SortDirection.ASC;
	}

	/**
	 * 判断是否为降序
	 */
	public boolean isDesc() {
		return direction == SortDirection.DESC;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SortItem sortItem = (SortItem) o;
		return Objects.equals(field, sortItem.field) && direction == sortItem.direction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(field, direction);
	}

	@Override
	public String toString() {
		return field + " " + direction;
	}

}
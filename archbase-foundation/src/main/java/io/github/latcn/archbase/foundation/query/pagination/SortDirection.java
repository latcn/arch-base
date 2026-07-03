package io.github.latcn.archbase.foundation.query.pagination;

/**
 * 排序方向枚举
 */
public enum SortDirection {

	/**
	 * 升序（ASC）
	 */
	ASC("ASC"),

	/**
	 * 降序（DESC）
	 */
	DESC("DESC");

	private final String sqlKeyword;

	SortDirection(String sqlKeyword) {
		this.sqlKeyword = sqlKeyword;
	}

	public String getSqlKeyword() {
		return sqlKeyword;
	}

	/**
	 * 判断是否为升序
	 */
	public boolean isAsc() {
		return this == ASC;
	}

	/**
	 * 判断是否为降序
	 */
	public boolean isDesc() {
		return this == DESC;
	}

	/**
	 * 根据字符串获取枚举
	 */
	public static SortDirection fromString(String value) {
		if (value == null || value.isEmpty()) {
			return ASC;
		}
		String upper = value.toUpperCase();
		if ("DESC".equals(upper) || "DESCENDING".equals(upper)) {
			return DESC;
		}
		return ASC;
	}

}
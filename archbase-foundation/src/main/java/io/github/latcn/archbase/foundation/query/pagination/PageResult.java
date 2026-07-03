package io.github.latcn.archbase.foundation.query.pagination;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页结果
 *
 * @param <T> 数据类型
 */
public class PageResult<T> {

	/** 总记录数 */
	private long total;

	/** 当前页数据 */
	private List<T> records;

	// ==================== 构造方法 ====================

	public PageResult() {
		this.records = Collections.emptyList();
	}

	public PageResult(long total, List<T> records) {
		this.total = total;
		this.records = records != null ? records : Collections.emptyList();
	}

	// ==================== 静态工厂方法 ====================

	public static <T> PageResult<T> of(long total, List<T> records) {
		return new PageResult<>(total, records);
	}

	public static <T> PageResult<T> empty() {
		return new PageResult<>(0, Collections.emptyList());
	}

	// ==================== Getter/Setter ====================

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records != null ? records : Collections.emptyList();
	}

	// ==================== 工具方法 ====================

	/**
	 * 判断是否为空结果
	 */
	public boolean isEmpty() {
		return total == 0 || records.isEmpty();
	}

	/**
	 * 判断是否非空结果
	 */
	public boolean isNotEmpty() {
		return !isEmpty();
	}

	/**
	 * 获取当前页记录数
	 */
	public int getRecordCount() {
		return records != null ? records.size() : 0;
	}

	/**
	 * 转换记录类型（映射）
	 */
	public <R> PageResult<R> map(Function<T, R> mapper) {
		if (records == null || records.isEmpty()) {
			return PageResult.empty();
		}
		List<R> mapped = records.stream().map(mapper).collect(Collectors.toList());
		return new PageResult<>(total, mapped);
	}

	@Override
	public String toString() {
		return "PageResult{" + "total=" + total + ", recordsSize=" + (records != null ? records.size() : 0) + '}';
	}

}
package io.github.latcn.archbase.foundation.query.pagination;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class PageResult<T> {

	private long total;

	private List<T> records;

	public PageResult() {
		this.records = Collections.emptyList();
	}

	public PageResult(long total, List<T> records) {
		this.total = total;
		this.records = records != null ? records : Collections.emptyList();
	}

	public static <T> PageResult<T> of(long total, List<T> records) {
		return new PageResult<>(total, records);
	}

	public static <T> PageResult<T> empty() {
		return new PageResult<>(0, Collections.emptyList());
	}

	public boolean isEmpty() {
		return total == 0 || records.isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public int getRecordCount() {
		return records != null ? records.size() : 0;
	}

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
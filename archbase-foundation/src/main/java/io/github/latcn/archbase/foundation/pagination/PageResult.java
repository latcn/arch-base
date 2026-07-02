package io.github.latcn.archbase.foundation.pagination;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records != null ? records : Collections.emptyList(); }

    public boolean isEmpty() {
        return total == 0 || records.isEmpty();
    }

    public <R> PageResult<R> map(Function<T, R> mapper) {
        List<R> mapped = records.stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new PageResult<>(total, mapped);
    }
}
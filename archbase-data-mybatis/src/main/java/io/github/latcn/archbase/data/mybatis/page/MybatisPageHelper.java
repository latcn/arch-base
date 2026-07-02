package io.github.latcn.archbase.data.mybatis.page;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.latcn.archbase.foundation.pagination.PageQuery;

public final class MybatisPageHelper {

    private MybatisPageHelper() {}

    public static <T> Page<T> toPage(PageQuery pageQuery) {
        return new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
    }

    public static <T> void applySort(PageQuery pageQuery, QueryWrapper<T> wrapper) {
        String sortField = pageQuery.getSortField();
        String sortOrder = pageQuery.getSortOrder();
        if (sortField != null && !sortField.isEmpty()) {
            if ("DESC".equalsIgnoreCase(sortOrder)) {
                wrapper.orderByDesc(sortField);
            } else {
                wrapper.orderByAsc(sortField);
            }
        }
    }
}
package io.github.latcn.archbase.foundation.query.pagination;

import io.github.latcn.archbase.core.api.IQuery;
import io.github.latcn.archbase.foundation.query.cond.Condition;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 统一查询请求（单表） 包含分页、排序、条件树
 */
@Data
public class PageQuery implements IQuery {

	/**
	 * 页码，从1开始
	 */
	private Integer pageNum = 1;

	/**
	 * 每页大小
	 */
	private Integer pageSize = 10;

	/**
	 * 是否查询总数
	 */
	private boolean searchCount = true;

	/**
	 * 排序列表
	 */
	private List<SortItem> sorts = new ArrayList<>();

	/**
	 * 条件树根节点
	 */
	private Condition condition;

}
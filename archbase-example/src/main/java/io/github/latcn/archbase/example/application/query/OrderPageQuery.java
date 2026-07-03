package io.github.latcn.archbase.example.application.query;

import io.github.latcn.archbase.foundation.query.pagination.PageQuery;
import lombok.Data;

@Data
public class OrderPageQuery extends PageQuery {

	private Long userId;

}
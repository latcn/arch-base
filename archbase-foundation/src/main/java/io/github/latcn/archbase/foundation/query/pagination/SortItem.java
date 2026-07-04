package io.github.latcn.archbase.foundation.query.pagination;

import java.util.Objects;
import lombok.Data;

@Data
public class SortItem {

	private String field;

	private SortDirection direction;

	public SortItem() {
	}

	public SortItem(String field, SortDirection direction) {
		this.field = field;
		this.direction = direction;
	}

	public static SortItem asc(String field) {
		return new SortItem(field, SortDirection.ASC);
	}

	public static SortItem desc(String field) {
		return new SortItem(field, SortDirection.DESC);
	}

	public boolean isAsc() {
		return direction == SortDirection.ASC;
	}

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
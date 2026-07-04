package io.github.latcn.archbase.foundation.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public abstract class BaseEntity<ID extends Serializable> {

	private ID id;

}
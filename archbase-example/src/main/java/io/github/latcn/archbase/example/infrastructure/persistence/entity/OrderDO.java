package io.github.latcn.archbase.example.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_order")
public class OrderDO {

	@TableId(type = IdType.AUTO)
	private Long id;

	private Long userId;

	private BigDecimal amount;

	private String status;

	@TableField("is_deleted")
	private Integer isDeleted;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
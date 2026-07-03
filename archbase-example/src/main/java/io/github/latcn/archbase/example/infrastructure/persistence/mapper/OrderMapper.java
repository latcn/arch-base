package io.github.latcn.archbase.example.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.latcn.archbase.example.infrastructure.persistence.po.OrderPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderPO> {

}
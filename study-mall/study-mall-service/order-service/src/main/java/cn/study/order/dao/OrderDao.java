package cn.study.order.dao;

import cn.study.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:56:34
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}

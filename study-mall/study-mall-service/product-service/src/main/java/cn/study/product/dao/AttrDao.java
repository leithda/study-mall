package cn.study.product.dao;

import cn.study.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性
 * 
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
	
}

package cn.study.ware.dao;

import cn.study.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:10:48
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}

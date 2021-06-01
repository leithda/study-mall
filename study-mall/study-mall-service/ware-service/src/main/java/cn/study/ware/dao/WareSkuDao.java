package cn.study.ware.dao;

import cn.study.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:10:48
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    /**
     * 判断当前sku是否有库存
     * @param skuId sku ID
     */
    Long getSkuStock(Long skuId);
}

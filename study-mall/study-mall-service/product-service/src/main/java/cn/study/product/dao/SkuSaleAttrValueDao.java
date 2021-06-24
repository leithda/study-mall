package cn.study.product.dao;

import cn.study.product.entity.SkuSaleAttrValueEntity;
import cn.study.product.entity.vo.SkuItemVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    /**
     * 根据spuID查询所有的销售属性组合
     * @param spuId spuID
     */
    List<SkuItemVo.SkuItemSaleAttrsVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    /**
     * 根据SkuID获取值列表
     * @param skuId skuID
     */
    List<String> getSkuSaleAttrValuesAsStringList(@Param("skuId") Long skuId);
}

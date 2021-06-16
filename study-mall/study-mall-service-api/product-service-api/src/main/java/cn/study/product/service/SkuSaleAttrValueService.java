package cn.study.product.service;

import cn.study.product.entity.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.SkuSaleAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据spuId的属性获取所有的销售属性组合
     * @param spuId spuId
     */
    List<SkuItemVo.SkuItemSaleAttrsVo> getSaleAttrsBySpuId(Long spuId);
}


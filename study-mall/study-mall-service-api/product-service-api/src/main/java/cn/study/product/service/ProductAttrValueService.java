package cn.study.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存Spu属性
     * @param productAttrValueEntityList spu属性
     */
    void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntityList);
}


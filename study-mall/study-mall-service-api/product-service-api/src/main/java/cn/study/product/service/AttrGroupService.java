package cn.study.product.service;

import cn.study.product.entity.vo.AttrGroupWithAttrsVo;
import cn.study.product.entity.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分页查询指定分类下的属性分组
     * @param params 请求参数
     * @param categoryId 分类ID
     */
    PageUtils queryPage(Map<String, Object> params, Long categoryId);

    /**
     * 根据分类ID获取属性分组与属性
     * @param catelogId 分类ID
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrByCatelogId(Long catelogId);

    /**
     * 根据 spuid 获取spu的属性规格信息
     * @param spuId spuID
     * @param catalogId 分类ID
     */
    List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrBySpuId(Long spuId, Long catalogId);
}


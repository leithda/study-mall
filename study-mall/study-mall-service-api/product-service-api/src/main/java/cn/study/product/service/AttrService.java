package cn.study.product.service;


import cn.study.product.entity.vo.AttrGroupRelationVo;
import cn.study.product.entity.vo.AttrRespVo;
import cn.study.product.entity.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attrVo);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);


    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    /**
     * 根据分组id，获取组内关联的所有属性
     * @param attrGroupId 属性分组ID
     */
    List<AttrEntity> getRelationAttr(Long attrGroupId);

    /**
     * 移除属性分组与属性的关联
     * @param attrGroupRelationVoList 参数
     */
    void deleteRelation(List<AttrGroupRelationVo> attrGroupRelationVoList);

    /**
     * 获取当前分组未关联的属性
     * @param params 分页参数
     * @param attrGroupId 分组ID
     */
    PageUtils getAttrNoRelation(Map<String, Object> params, Long attrGroupId);

    /**
     * 获取给定属性集合中可以被检索的规格属性
     * @param attrIds 规格属性Id集合
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}


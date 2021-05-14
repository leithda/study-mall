package cn.study.product.service;


import cn.study.product.entity.vo.AttrRespVo;
import cn.study.product.entity.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.AttrEntity;

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

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId);


    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);
}


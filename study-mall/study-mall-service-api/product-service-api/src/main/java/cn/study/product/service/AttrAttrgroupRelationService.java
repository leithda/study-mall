package cn.study.product.service;

import cn.study.product.entity.vo.AttrGroupRelationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBatch(List<AttrGroupRelationVo> attrGroupRelationVoList);

}


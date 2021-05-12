package cn.study.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.AttrGroupEntity;

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
}


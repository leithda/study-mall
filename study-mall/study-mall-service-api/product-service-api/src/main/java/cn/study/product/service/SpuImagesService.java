package cn.study.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存Spu的图片
     * @param id spuId
     * @param images 图片
     */
    void saveImages(Long id, List<String> images);
}


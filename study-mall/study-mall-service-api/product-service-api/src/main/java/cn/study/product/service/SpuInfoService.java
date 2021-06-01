package cn.study.product.service;

import cn.study.product.entity.SpuInfoDescEntity;
import cn.study.product.entity.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存商品信息
     * @param vo 商品信息
     */
    void saveSpuInfo(SpuSaveVo vo);

    /**
     * 保存 spu 基本信息
     * @param spuInfoEntity spu基本信息
     */
    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    /**
     * 保存 spu 图片信息
     * @param infoDescEntity spu 图片信息
     */
    void saveSpuInfoDesc(SpuInfoDescEntity infoDescEntity);

    /**
     * 根据条件分页查询 Spu 列表
     * @param params 参数
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 商品上架
     * @param spuId 上架商品 spuId
     */
    void up(Long spuId);
}


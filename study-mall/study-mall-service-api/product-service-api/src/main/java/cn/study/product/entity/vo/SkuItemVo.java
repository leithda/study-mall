package cn.study.product.entity.vo;

import cn.study.product.entity.SkuImagesEntity;
import cn.study.product.entity.SkuInfoEntity;
import cn.study.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {
    // 1、sku基本信息 pms_sku_info
    SkuInfoEntity info;

    /**
     * 是否有货
     */
    boolean hasStock = true;

    // 2、sku的图片信息 pms_sku_images
    List<SkuImagesEntity> images;

    // 3、spu的销售属性组合
    List<SkuItemSaleAttrsVo> saleAttr;

    // 4、获取spu的介绍
    SpuInfoDescEntity desp;

    // 5、获取spu的规格参数信息
    List<SpuItemAttrGroupVo> groupAttrs;

    /**
     * Sku 销售属性分装
     */
    @Data
    public static class SkuItemSaleAttrsVo{
        private Long attrId;
        /**
         * 属性名
         */
        private String attrName;

        /**
         * 属性值
         */
        private List<AttrValueWithSkuIdVo> attrValues;
    }


    /**
     * Spu基础属性数据分装
     */
    @Data
    public static class SpuItemAttrGroupVo{

        /**
         * 分组名
         */
        private String groupName;

        private List<SpuBaseAttrVo> attrs;
    }

    /**
     * spu基础属性对象封装
     */
    @Data
    public static class SpuBaseAttrVo {
        private String attrName;
        private String attrValue;
    }
}

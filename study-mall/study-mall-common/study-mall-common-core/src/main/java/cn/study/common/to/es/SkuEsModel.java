package cn.study.common.to.es;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuEsModel {

    /**
     * SkuID
     */
    private Long skuId;

    /**
     * SpuID
     */
    private Long spuId;

    /**
     * sku标题
     */
    private String skuTitle;

    /**
     * sku价格
     */
    private BigDecimal skuPrice;

    /**
     * sku默认图片
     */
    private String skuImg;

    /**
     * 销量
     */
    private Long saleCount;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * 热度评分
     */
    private Long hotScore;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 分类ID
     */
    private Long catelogId;

    /**
     * 品牌名字
     */
    private String brandName;

    /**
     * 品牌图片
     */
    private String brandImg;

    /**
     * 分类名称
     */
    private String catelogName;

    private List<Attrs> attrs;


    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}

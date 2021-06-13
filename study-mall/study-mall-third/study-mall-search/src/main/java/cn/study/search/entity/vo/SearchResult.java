package cn.study.search.entity.vo;

import cn.study.search.entity.EsProductEntity;
import lombok.Data;

import java.util.List;

/**
 * 检索商品结果
 */
@Data
public class SearchResult {

    /**
     * 商品信息
     */
    List<EsProductEntity> products;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页码
     */
    private Integer totalPages;

    /**
     * 前端导航分页
     */
    private List<Integer> pageNavs;

    /**
     * 所有品牌
     */
    List<BrandVo> brands;

    /**
     * 所有分类
     */
    List<CatelogVo> catelogs;

    /**
     * 所有属性
     */
    List<AttrVo> attrs;

    // ================= 以上是返回给页面的所有信息 =================

    /**
     * 品牌
     */
    @Data
    public static class BrandVo {
        private Long brandId;
        /**
         * 品牌名字
         */
        private String brandName;

        /**
         * 品牌图片
         */
        private String brandImg;
    }


    /**
     * 规格属性
     */
    @Data
    public static class AttrVo {
        private Long attrId;
        /**
         * 属性名字
         */
        private String attrName;
        /**
         * 属性值
         */
        private List<String> attrValue;
    }

    /**
     * 分类
     */
    @Data
    public static class CatelogVo {
        private Long catelogId;
        /**
         * 分类名字
         */
        private String catelogName;
    }
}

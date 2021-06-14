package cn.study.search.entity.vo;


import lombok.Data;

import java.util.List;

/**
 * 封装页面检索条件
 * catelog3Id=225&keyword=小米&sort=saleCount_desc&hasStock=1&skuPrice=1500_4000&brandId=1&brandId=2&attrs=1_其他:安卓&attrs=2_6.4寸:5寸
 */
@Data
public class SearchParam {
    /**
     * 页面参数，全文匹配关键字 skuTitle->keyword
     */
    private String keyword;

    /**
     * 三级分类Id
     */
    private Long catelog3Id;

    /**
     * 排序条件 saleCount、hotScore、skuPrice
     */
    private String sort;

    /**
     * 分页参数
     */
    private Integer pageNum = 1;

    /**
     * 是否只显示有货
     */
    private Integer hasStock;

    /**
     * 价格区间
     */
    private String skuPrice;

    /**
     * 按照品牌进行查询，可以多选 brandId=1&brandId=2
     */
    private List<Long> brandId;

    /**
     * 按照属性筛选，可以选多个值，用:分隔 attrs=1_其他:安卓&attrs=2_6.4寸:5寸
     */
    private List<String> attrs;

    /**
     * 请求
     */
    private String _queryString;

}

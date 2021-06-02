package cn.study.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 二级分类实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {
    /** 2级分类id */
    private String id;

    /** 分类名字 */
    private String name;

    /** 1级父分类id */
    private String catalog1Id;

    /** 3级子分类 */
    private List<Catelog3Vo> catalog3List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        /** 2级父分类Id */
        private String catalog2Id;
        private String id;
        private String name;


    }

}

package cn.study.order.entity.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemVo {

    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;


    @Getter(AccessLevel.NONE)
    private BigDecimal totalPrice;

    // TODO: 2021/6/30 查询库存状态
    private boolean hasStock;
    private BigDecimal weight;
}

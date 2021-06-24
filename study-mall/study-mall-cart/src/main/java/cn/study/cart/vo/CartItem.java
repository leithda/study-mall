package cn.study.cart.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物项
 */
@Data
public class CartItem {
    private Long skuId;
    private boolean check = true;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;

    @Getter(AccessLevel.NONE)
    private BigDecimal totalPrice;

    /**
     * 计算购物项总价
     */
    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal(count));
    }


}

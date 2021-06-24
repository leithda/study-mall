package cn.study.cart.vo;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车对象
 */
@Data
public class Cart {
    /**
     * 商品明细
     */
    private List<CartItem> items;
    /**
     * 商品数量
     */
    private Integer countNum;

    /**
     * 商品类型数量
     */
    private Integer countType;

    /**
     * 商品总价
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 优惠价格
     */
    private BigDecimal reduce;


    public Integer getCountNum() {
        int count = 0;
        if (CollectionUtils.isNotEmpty(items)) {
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    public Integer getCountType() {
        return items.size();
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(items)) {
            for (CartItem cartItem : items) {
                if (cartItem.isCheck()) {
                    amount = amount.add(cartItem.getTotalPrice());
                }
            }
            return amount;
        }
        return amount;
    }
}

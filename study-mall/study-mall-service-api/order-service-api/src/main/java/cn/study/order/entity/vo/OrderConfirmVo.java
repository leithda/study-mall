package cn.study.order.entity.vo;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 订单确认页所需数据
 */
@Data
public class OrderConfirmVo {
    // 收货地址
    List<MemberAddressVo> address;

    // 送货清单
    List<OrderItemVo> items;

    // 发票...

    // 优惠券...
    /**
     * 积分
     */
    private Integer integration;

    /**
     * 防重订单令牌
     */
    private String orderToken;

    /**
     * 获取商品件数
     */
    public Integer getCount(){
        Integer count = 0;
        if(Objects.nonNull(items)){
            for (OrderItemVo item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    /**
     * 订单总额
     */
    public BigDecimal getTotal() {
        BigDecimal bigTotal = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(items)) {
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal("" + item.getCount()));
                bigTotal = bigTotal.add(multiply);
            }
        }
        return bigTotal;
    }

    /**
     * 应付金额
     */
    public BigDecimal getPayPrice() {
        BigDecimal bigTotal = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(items)) {
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal("" + item.getCount()));
                bigTotal = bigTotal.add(multiply);
            }
        }
        return bigTotal;
    }
}

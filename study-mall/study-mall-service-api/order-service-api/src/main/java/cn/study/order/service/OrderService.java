package cn.study.order.service;

import cn.study.order.entity.vo.OrderConfirmVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:56:34
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 返回订单确认页所需要的数据
     */
    OrderConfirmVo confirmOrder();
}


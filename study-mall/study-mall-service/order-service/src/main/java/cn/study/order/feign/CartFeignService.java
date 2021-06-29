package cn.study.order.feign;

import cn.study.common.constant.ServiceNameConstant;
import cn.study.order.entity.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(ServiceNameConstant.CART_SERVICE)
public interface CartFeignService {

    @GetMapping("currentUserCartItems")
    List<OrderItemVo> currentUserCartItems();
}

package cn.study.order.service.impl;

import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;
import cn.study.order.dao.OrderDao;
import cn.study.order.entity.OrderEntity;
import cn.study.order.entity.vo.MemberAddressVo;
import cn.study.order.entity.vo.OrderConfirmVo;
import cn.study.order.entity.vo.OrderItemVo;
import cn.study.order.feign.CartFeignService;
import cn.study.order.feign.MemberFeignService;
import cn.study.order.interceptor.LoginUserInterceptor;
import cn.study.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    CartFeignService cartFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        LinkedHashMap loginUser = LoginUserInterceptor.threadLocal.get();
        Long userId = Long.parseLong(loginUser.get("id")+"");

        // 1、远程查询用户的收货地址列表
        List<MemberAddressVo> address = memberFeignService.getAddress(userId);
        orderConfirmVo.setAddress(address);

        // 2、远程购物车所有选中的购物项
        List<OrderItemVo> items = cartFeignService.currentUserCartItems();
        orderConfirmVo.setItems(items);

        // feign 在远程调用之前要构造请求，调用很多的拦截器
        /* #SynchronousMethodHandler.targetRequest
        for (RequestInterceptor interceptor : requestInterceptors) {
          interceptor.apply(template);
        }
        * */

        // 3、获取用户积分信息
        Integer integration = (Integer) loginUser.get("integration");
        orderConfirmVo.setIntegration(integration);

        // 4、其他的数据自动计算


        return null;
    }

}